package com.jsaw.ibreast;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class login extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private EditText etEmail;
    private EditText etPassword;
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    public String email;
    private String Uid;
    // InputMethodManager imm;
    // private View customView;

    public static class Users {
        public String email;
        public String weight = "";
        public String height = "";
        Users(String email) {
            this.email = email;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn_signUp_main = findViewById(R.id.btn_signup_main);
        // customView = LayoutInflater.from(login.this).inflate(R.layout.dialog_email, null);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        //user
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("onAuthStateChanged", "登入:" + user.getEmail());
                    Log.d("onAuthStateChanged", "登入:" + auth.getUid());
                    Uid = user.getUid();
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //如果DB沒有User，則建立
                            if (!dataSnapshot.hasChild(Uid)){
                                Users users = new Users(user.getEmail());
                                mDatabase.child(Uid).setValue(users);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    if (isProgressDialogShow) {
                        progressDialog.dismiss();
                        isProgressDialogShow = false;
                    }
                    startActivity(new Intent().setClass(login.this, main.class));
                    finish();
                }
            }
        };
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        btn_signUp_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, register.class));
            }
        });

        TextView forgotPassword = findViewById(R.id.forgotPW);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new AlertDialog.Builder(login.this)
//                        .setTitle("重設密碼")
//                        .setView(customView)
//                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                EditText editText = customView.findViewById(R.id.dialog_input);
//                                String mail = editText.getText().toString();
//                                if(TextUtils.isEmpty(mail))Toast.makeText(login.this,"電子郵件不可為空", Toast.LENGTH_SHORT).show();
//                                else forgotPassword(mail);
//                            }
//                        })
//                        .show();
                String email = etEmail.getText().toString().trim();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    new AlertDialog.Builder(login.this)
                            .setTitle("提示")
                            .setMessage("帳號格式錯誤!!\n請輸入正確帳號以找回密碼")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    etEmail.setText("");
                } else forgotPassword(email);
            }
        });
    }

    private void forgotPassword(String mail) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(mail);
        new AlertDialog.Builder(login.this)
                .setTitle("提示")
                .setMessage("密碼重設信件已發送至您的電子信箱\n請至您的信箱收取信件!")
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    //double back to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再按一次返回退出APP", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    //    //hide keyboard
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(event.getAction() == MotionEvent.ACTION_DOWN && getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//        return super.onTouchEvent(event);
//    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void checkLogin(View view) {
        String mail = etEmail.getText().toString().trim();
        String pw = etPassword.getText().toString().trim();
        if (!mail.equals("") && !pw.equals("")) {
            auth.signInWithEmailAndPassword(mail, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        if (isProgressDialogShow) {
                            progressDialog.dismiss();
                            isProgressDialogShow = false;
                        }
                        Toast.makeText(login.this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //waiting dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("處理中,請稍候...");
            progressDialog.show();
            isProgressDialogShow = true;
            //connect time out
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isProgressDialogShow) {
                        progressDialog.dismiss();
                        Toast.makeText(login.this, "連線逾時", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 3000);
        } else Toast.makeText(this, "帳號密碼不可為空", Toast.LENGTH_SHORT).show();
    }


}

