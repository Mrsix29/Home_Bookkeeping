package ca.nait.zli.homebookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.nait.zli.homebookkeeping.adapter.AccountAdapter;
import ca.nait.zli.homebookkeeping.db.AccountBean;
import ca.nait.zli.homebookkeeping.db.DBManager;

public class SearchActivity extends AppCompatActivity {

    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    List<AccountBean>accountData;   //数据源
    AccountAdapter adapter;    //适配器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        accountData = new ArrayList<>();
        adapter = new AccountAdapter(this,accountData);
        searchLv.setAdapter(adapter);
        searchLv.setEmptyView(emptyTv);   //无数据时，显示的控件
    }

    private void initView() {
        searchEt = findViewById(R.id.search_et);
        searchLv = findViewById(R.id.search_lv);
        emptyTv = findViewById(R.id.search_tv_empty);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:   //执行搜索的操作
                String msg = searchEt.getText().toString().trim();
//                判断输入内容是否为空，如果为空，就提示不能搜索
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this,"Please enter record information",Toast.LENGTH_SHORT).show();
                    return;
                }
                //开始搜索
                List<AccountBean> list = DBManager.getAccountListByNoteFromRecord(msg);
                accountData.clear();
                accountData.addAll(list);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}