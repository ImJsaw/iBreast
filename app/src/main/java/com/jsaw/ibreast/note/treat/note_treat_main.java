package com.jsaw.ibreast.note.treat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jsaw.ibreast.R;

import java.util.HashMap;
import java.util.Map;

public class note_treat_main extends AppCompatActivity {

    //透過下方程式碼，取得Activity中執行的個體。
    private FragmentTransaction transaction;
    private int btnC = 1;
    private Map<Integer, Fragment> myView = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_treat);
        transaction = getSupportFragmentManager().beginTransaction();

//         first fragment is activity_note_treat1
        transaction.replace(R.id.center, new note_treat1(), "fragment1");
        transaction.commit();
        setMyView();

    }

    //換頁的按鈕設定
    public void onClick(View view) {
        transaction = getSupportFragmentManager().beginTransaction();
        //傳回被按的button
        switch (view.getId()) {
            case R.id.btn1:
                btnC = 1;
                setColor(btnC);
                transaction.replace(R.id.center, new note_treat1());
                break;
            case R.id.btn2:
                btnC = 2;
                setColor(btnC);
                transaction.replace(R.id.center, new note_treat2());
                break;
            case R.id.btn3:
                btnC = 3;
                setColor(btnC);
                transaction.replace(R.id.center, new note_treat3());
                break;
            case R.id.btn4:
                btnC = 4;
                setColor(btnC);
                transaction.replace(R.id.center, new note_treat3());
                break;
            case R.id.btn5:
                btnC = 5;
                setColor(btnC);
                transaction.replace(R.id.center, new note_treat3());
                break;
            case R.id.Btn_edit:
                // 目前所在fragment
                Log.d("note_treat_main", String.valueOf(btnC));
                transaction.replace(R.id.center, myView.get(btnC));
                break;
        }
        //呼叫commit讓變更生效。
        transaction.commit();
    }

    public void setColor(int btnC) {
        int countFragment = 5;
        Button btn[] = new Button[]{findViewById(R.id.btn1), findViewById(R.id.btn2),
                findViewById(R.id.btn3), findViewById(R.id.btn4), findViewById(R.id.btn5)};

        for (int i = 0; i < countFragment; i++) {
            if (i == btnC - 1) {
                btn[i].setTextColor(Color.WHITE);
            } else {
                btn[i].setTextColor(Color.GRAY);
            }
        }
    }

    public void setMyView() {
        myView.put(1, new note_treat1_add());
        myView.put(2, new note_treat2_add());
        myView.put(3, new note_treat3_add());
        myView.put(4, new note_treat4_add());
        myView.put(5, new note_treat5_add());
    }
}
