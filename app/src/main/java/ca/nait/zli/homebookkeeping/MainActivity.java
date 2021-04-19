package ca.nait.zli.homebookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.nait.zli.homebookkeeping.adapter.AccountAdapter;
import ca.nait.zli.homebookkeeping.db.AccountBean;
import ca.nait.zli.homebookkeeping.db.DBManager;
import ca.nait.zli.homebookkeeping.utils.BudgetDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayListView;  //展示今日收支情况的ListView
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    //声明数据源
    List<AccountBean> accountData;
    AccountAdapter adapter;
    int year, month, day;
    //头布局相关控件
    View summaryView;
    TextView monthlyExpenseTextView, monthlyIncomeTextView, budgetTextView, todaySummaryTextView;
    ImageView toggleImageView;
    SharedPreferences preferences;
    boolean isShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        addSummaryView();
        accountData = new ArrayList<>();
        adapter = new AccountAdapter(this, accountData);
        todayListView.setAdapter(adapter);
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
    }

    private void initView() {
        todayListView = findViewById(R.id.main_list_view);
        editBtn = findViewById(R.id.main_button_edit);
        moreBtn = findViewById(R.id.main_button_more);
        searchIv = findViewById(R.id.main_image_view_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setSummaryTextView();

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromRecord(year, month, day);
        accountData.clear();
        accountData.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_image_view_search:
                Intent search = new Intent(this, SearchActivity.class);  //跳转界面
                startActivity(search);
                break;
            case R.id.main_button_edit:
                Intent edit = new Intent(this, RecordActivity.class);  //跳转界面
                startActivity(edit);
                break;
/*           case R.id.main_button_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;

 */
            case R.id.monthly_summary_main_text_view_balance_cost:
                showBudgetDialog();
                break;
            case R.id.monthly_summary_main_image_view:
                toggleShow();
                break;
        }
/*        if (view == summaryView) {
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }*/
    }


    /* 弹出是否删除某一条记录的对话框*/
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Record？").setMessage("Do you really want to delete the record? This process cannot be undone.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManager.deleteItemFromRecordById(click_id);
                        accountData.remove(clickBean);   //实时刷新，移除集合当中的对象
                        adapter.notifyDataSetChanged();   //提示适配器更新数据
                        setSummaryTextView();   //改变头布局TextView显示的内容
                    }
                });
        builder.create().show();   //显示对话框
    }

    /**
     * 给ListView添加头布局的方法
     */
    private void addSummaryView() {
        //将布局转换成View对象
        summaryView = getLayoutInflater().inflate(R.layout.mothly_summary_main, null);
        todayListView.addHeaderView(summaryView);
        //查找头布局可用控件
        monthlyExpenseTextView = summaryView.findViewById(R.id.monthly_summary_main_text_view_expense_cost);
        monthlyIncomeTextView = summaryView.findViewById(R.id.monthly_summary_main_text_view_income_cost);
        budgetTextView = summaryView.findViewById(R.id.monthly_summary_main_text_view_balance_cost);
        todaySummaryTextView = summaryView.findViewById(R.id.monthly_summary_main_text_view_today);
        toggleImageView = summaryView.findViewById(R.id.monthly_summary_main_image_view);

        budgetTextView.setOnClickListener(this);
        summaryView.setOnClickListener(this);
        toggleImageView.setOnClickListener(this);

    }

    /* 设置头布局当中文本内容的显示*/
    private void setSummaryTextView() {
        //获取今日支出和收入总金额，显示在view当中
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float expenseOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "Today Expense $" + expenseOneDay + "  Income $" + incomeOneDay;
        todaySummaryTextView.setText(infoOneDay);
//        获取本月收入和支出总金额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float expenseOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        monthlyIncomeTextView.setText("$" + incomeOneMonth);
        monthlyExpenseTextView.setText("$" + expenseOneMonth);

        //    设置显示运算剩余
        float budget = preferences.getFloat("budget", 0);//预算
        if (budget == 0) {
            budgetTextView.setText("$ 0");
        }else{
            float balance = budget-expenseOneMonth;
            budgetTextView.setText("$"+balance);
        }

    }

    private void toggleShow() {
        if (isShow) {   //明文====》密文
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            monthlyIncomeTextView.setTransformationMethod(passwordMethod);   //设置隐藏
            monthlyExpenseTextView.setTransformationMethod(passwordMethod);   //设置隐藏
            budgetTextView.setTransformationMethod(passwordMethod);   //设置隐藏
            toggleImageView.setImageResource(R.mipmap.ic_hide);
            isShow = false;   //设置标志位为隐藏状态
        }else{  //密文---》明文
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            monthlyIncomeTextView.setTransformationMethod(hideMethod);   //设置隐藏
            monthlyExpenseTextView.setTransformationMethod(hideMethod);   //设置隐藏
            budgetTextView.setTransformationMethod(hideMethod);   //设置隐藏
            toggleImageView.setImageResource(R.mipmap.ic_show);
            isShow = true;   //设置标志位为隐藏状态
        }
    }

    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额写入到共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("budget",money);
                editor.commit();
                //计算剩余金额
                float expenseOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float balance = money-expenseOneMonth;//预算剩余 = 预算-支出
                budgetTextView.setText("$"+balance);
            }
        });
    }
    private void setLVLongClickListener() {
        todayListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  //点击了头布局
                    return false;
                }
                int pos = position-1;
                AccountBean clickBean = accountData.get(pos);  //获取正在被点击的这条信息

                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
}