package com.jsaw.ibreast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {
    private EditText edt_email;
    private EditText edt_password;
    private EditText edt_confirm;
    private boolean email_valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edt_email = findViewById(R.id.email);
        edt_password = findViewById(R.id.password);
        edt_confirm = findViewById(R.id.confirm);
        Button btn_signup = findViewById(R.id.btn_signup);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                String confirm = edt_confirm.getText().toString().trim();
                boolean correct = checkInput(email, password, confirm);

                if(correct){
                    //todo register
                }
                else{
                    if(!email_valid) edt_email.setText("");
                    edt_password.setText("");
                    edt_confirm.setText("");
                }
            }
        });
    }

    boolean checkInput(String email, String password, String confirm){
        if (TextUtils.isEmpty(email)) {
            email_valid=false;
            Toast.makeText(getApplication(), "Email不可為空", Toast.LENGTH_SHORT).show();
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_valid=false;
            Toast.makeText(getApplication(), "Email格式錯誤", Toast.LENGTH_SHORT).show();
        }
        else {
            email_valid=true;
            if (TextUtils.isEmpty(password)|| TextUtils.isEmpty(confirm)) {
                Toast.makeText(getApplication(), "密碼不可為空", Toast.LENGTH_SHORT).show();
            }else if(password.length()>12||password.length()<6){
                Toast.makeText(getApplication(), "密碼長度不符", Toast.LENGTH_SHORT).show();
            }
            else if (!password.equals(confirm)) {
                Toast.makeText(getApplication(), "密碼不符合", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "註冊成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    InputMethodManager imm;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return super.onTouchEvent(event);
    }

    //action bar "back"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
