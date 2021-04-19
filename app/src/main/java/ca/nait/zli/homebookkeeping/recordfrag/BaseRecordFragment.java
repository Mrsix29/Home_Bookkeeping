package ca.nait.zli.homebookkeeping.recordfrag;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.nait.zli.homebookkeeping.R;
import ca.nait.zli.homebookkeeping.db.AccountBean;
import ca.nait.zli.homebookkeeping.db.TypeBean;
import ca.nait.zli.homebookkeeping.utils.KeyBoardUtils;
import ca.nait.zli.homebookkeeping.utils.NoteDialog;
import ca.nait.zli.homebookkeeping.utils.SelectTimeDialog;

public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEditText;
    ImageView typeImageView;
    TextView typeTextView,noteTextView,timeTextView;
    GridView typeGridView;
    List<TypeBean> typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_record, container, false);
        initView(view);
        setInitTime();
        //给GridView填充数据的方法
        loadDataToGV();
        setGVListener(); //设置GridView每一项的点击事件
        return view;
    }
    //  set the init time, and show it at text view
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = simpleDateFormat.format(date);
        timeTextView.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }



    private void setGVListener() {
        typeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated();  //提示绘制发生变化了
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTextView.setText(typename);
                accountBean.setTypeName(typename);
                int selectedImageId = typeBean.getSelectedImageId();
                typeImageView.setImageResource(selectedImageId);
                accountBean.setSelectedImageId(selectedImageId);
            }
        });
    }

    /* 给GridView填出数据的方法*/
    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGridView.setAdapter(adapter);
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.fragment_record_keyboard);
        moneyEditText = view.findViewById(R.id.fragment_record_edit_text_money);
        typeImageView = view.findViewById(R.id.fragment_record_image_view);
        typeGridView = view.findViewById(R.id.fragment_record_grid_view);
        typeTextView = view.findViewById(R.id.fragment_record_text_view_type);
        noteTextView = view.findViewById(R.id.fragment_record_text_view_note);
        timeTextView = view.findViewById(R.id.fragment_record_text_view_time);
        noteTextView.setOnClickListener(this);

        timeTextView.setOnClickListener(this);
        //show the customized keyboard
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEditText);
        boardUtils.showKeyboard();
        // listen the enter button
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //get the money
                String moneyStr = moneyEditText.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //get the information and save to DB
                saveAccountToDB();
                // back to last activity
                getActivity().finish();
            }
        });
    }
    /* Son Class will override the method*/
    public abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_record_text_view_time:
                showTimeDialog();
                break;
            case R.id.fragment_record_text_view_note:
                showNoteDialog();
                break;
        }
    }

    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTextView.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }


    public  void showNoteDialog(){
        final NoteDialog dialog = new NoteDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(() -> {
            String msg = dialog.getEditText();
            if (!TextUtils.isEmpty(msg)) {
                noteTextView.setText(msg);
                accountBean.setNote(msg);
            }
            dialog.cancel();
        });
    }
}
