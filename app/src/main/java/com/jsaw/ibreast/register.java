package com.jsaw.ibreast;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    private EditText edt_email;
    private EditText edt_password;
    private EditText edt_confirm;
    private boolean email_valid;
    ProgressDialog progressDialog;
    Boolean isProgressDialogShow = false;

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
                    //waiting dialog
                    progressDialog = new ProgressDialog(register.this);
                    progressDialog.setMessage("處理中,請稍候...");
                    progressDialog.show();
                    isProgressDialogShow = true;
                    //connect time out
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isProgressDialogShow){
                                progressDialog.dismiss();
                                Toast.makeText(register.this, "連線逾時", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 3000);
                    registerRequest(email,password);
                }
                else {
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
            } else return true;
        }
        return false;
    }

    //hide keyboard
    InputMethodManager imm;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return super.onTouchEvent(event);
    }

    private void registerRequest(String email,String password){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    isProgressDialogShow = false;
                                    AlertDialog registerSuccess = new AlertDialog.Builder(register.this)
                                            .setTitle("提示")
                                            .setMessage("註冊成功!")
                                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();   //jump to login page
                                                }
                                            })
                                            .create();
                                    registerSuccess.setCancelable(false);
                                    registerSuccess.show();
                                }
                                else{
                                    progressDialog.dismiss();
                                    isProgressDialogShow = false;
                                    Toast.makeText(register.this, "註冊失敗", Toast.LENGTH_SHORT).show();
                                    edt_email.setText("");
                                }
                            }
                        });
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
