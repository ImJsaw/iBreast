package com.jsaw.ibreast.note.body;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jsaw.ibreast.R;
import com.jsaw.ibreast.note.treat.note_treat5_add;

import java.sql.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class note_body extends AppCompatActivity{
    //透過下方程式碼，取得Activity中執行的個體。
    private FragmentTransaction transaction;
    private Map<Integer, Fragment> myView = new HashMap<>();
    private int btnCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_body);
        transaction = getSupportFragmentManager().beginTransaction();

//         first fragment is activity_note_treat1
        transaction.replace(R.id.center, new note_body1(), "fragment1");
        transaction.commit();
        setMyView();
    }

    //換頁的按鈕設定
    public void onClick(View view) {
        transaction = getSupportFragmentManager().beginTransaction();
        //傳回被按的button
        switch (view.getId()) {
            case R.id.btn1:
                btnCount = 1;
                setColor(btnCount);
                transaction.replace(R.id.center, new note_body1());
                break;
            case R.id.btn2:
                btnCount = 2;
                setColor(btnCount);
                transaction.replace(R.id.center, new note_body2());
                break;
            case R.id.btn3:
                btnCount = 3;
                setColor(btnCount);
                transaction.replace(R.id.center, new note_body3());
                break;
            case R.id.btn4:
                btnCount = 4;
                setColor(btnCount);
                transaction.replace(R.id.center, new note_body4());
                break;
            case R.id.btn5:
                btnCount = 5;
                setColor(btnCount);
                transaction.replace(R.id.center, new note_body5());
                break;
            case R.id.btn6:
                btnCount = 6;
                setColor(btnCount);
                transaction.replace(R.id.center, new note_body6());
                break;
            case R.id.Btn_edit:
                // 目前所在fragment
                Log.d("body_main", String.valueOf(btnCount));
//                Button btnEdit = findViewById(R.id.Btn_edit);
//                btnEdit.setText("儲存");
                transaction.replace(R.id.center, myView.get(btnCount));
                break;
        }
        //呼叫commit讓變更生效。
        transaction.commit();
    }

    public void setColor(int btnCount) {
        int countFragment = 6;
        Button btn[] = new Button[]{findViewById(R.id.btn1), findViewById(R.id.btn2),
                findViewById(R.id.btn3), findViewById(R.id.btn4), findViewById(R.id.btn5), findViewById(R.id.btn6)};

        List<Integer> chosen = Arrays.asList(R.drawable.body1, R.drawable.body2, R.drawable.body3,
                R.drawable.body4, R.drawable.body5, R.drawable.body6);

        List<Integer> unchosen = Arrays.asList(R.drawable.body11, R.drawable.body12, R.drawable.body13,
                R.drawable.body14, R.drawable.body15, R.drawable.body16);

        for (int i = 0; i < countFragment; i++) {
            if (i == btnCount - 1) {
                btn[i].setBackgroundResource(chosen.get(i));
            } else {
                btn[i].setBackgroundResource(unchosen.get(i));
            }
        }
    }

    public void setMyView() {
        myView.put(1, new body1_add());
        myView.put(2, new body2_add());
        myView.put(3, new body3_add());
        myView.put(4, new body4_add());
        myView.put(5, new body5_add());
        myView.put(6, new body6_add());
    }
}
