package com.jsaw.ibreast.note;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jsaw.ibreast.R;

public class note_treat_main extends AppCompatActivity {
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);

        //透過下方程式碼，取得Activity中執行的個體。
        manager = getSupportFragmentManager();
    }

    //換頁的按鈕設定
    public void onClick(View view) {
        //透過下方程式碼，取得Activity中執行的個體。
        transaction = manager.beginTransaction();
        switch (view.getId()) {
            case R.id.btn1:
                note_treat1 fragment1 = new note_treat1();
                transaction.replace(R.id.center, fragment1, "fragment1");

                break;
            case R.id.btn2:
                note_treat2 fragment2 = new note_treat2();
                transaction.replace(R.id.center, fragment2, "fragment2");
                break;
            case R.id.btn3:
                note_treat3 fragment3 = new note_treat3();
                transaction.replace(R.id.center, fragment3, "fragment3");
                break;
            case R.id.btn4:
                note_treat3 fragment4 = new note_treat3();
                transaction.replace(R.id.center, fragment4, "fragment4");
                break;
            case R.id.btn5:
                note_treat3 fragment5 = new note_treat3();
                transaction.replace(R.id.center, fragment5, "fragment5");
                break;
            case R.id.Btn_edit:
                note_treat2_add fragment2a = new note_treat2_add();
                transaction.replace(R.id.center, fragment2a, "fragment2a");
                break;
        }
//呼叫commit讓變更生效。
        transaction.commit();
    }
}
