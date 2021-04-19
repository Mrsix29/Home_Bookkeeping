package ca.nait.zli.homebookkeeping.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import ca.nait.zli.homebookkeeping.R;

public class NoteDialog extends Dialog implements View.OnClickListener {
    EditText editText;
    Button cancelBtn,saveBtn;
    OnEnsureListener onEnsureListener;
    // 设定回调接口的方法
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public NoteDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_note);//设置对话框显示布局
        editText = findViewById(R.id.dialog_note_edit_text);
        cancelBtn = findViewById(R.id.dialog_note_button_cancel);
        saveBtn = findViewById(R.id.dialog_note_button_save);
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }
    public interface OnEnsureListener{
        public void onEnsure();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_note_button_cancel:
                cancel();
                break;
            case R.id.dialog_note_button_save:
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

//    get edit text
    public String getEditText(){
        return editText.getText().toString().trim();
    }


    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
//        get the width of the window
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,100);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //automatically show the soft keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
           // toggle means if the keyboard is on, turn it off. if the keyboard is off, turn it on.
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
