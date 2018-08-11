package com.jsaw.ibreast.note.body;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jsaw.ibreast.R;
import com.jsaw.ibreast.note.treat.note_treat1;
import com.jsaw.ibreast.note.treat.note_treat2;
import com.jsaw.ibreast.note.treat.note_treat2_add;
import com.jsaw.ibreast.note.treat.note_treat3;

public class note_body extends AppCompatActivity{
    //透過下方程式碼，取得Activity中執行的個體。
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_body);
        transaction = getSupportFragmentManager().beginTransaction();

//         first fragment is note_treat1
        transaction.replace(R.id.center, new note_body1(), "fragment1");
        transaction.commit();
    }

    //換頁的按鈕設定
    public void onClick(View view) {
        transaction = getSupportFragmentManager().beginTransaction();
        //傳回被按的button
        int btnC;
        switch (view.getId()) {
            case R.id.btn1:
                btnC = 1;
                setColor(btnC);
                transaction.replace(R.id.center, new note_body1(), "fragment1");
                break;
            case R.id.btn2:
                btnC = 2;
                setColor(btnC);
                transaction.replace(R.id.center, new note_body2(), "fragment2");
                break;
            case R.id.btn3:
                btnC = 3;
                setColor(btnC);
                transaction.replace(R.id.center, new note_body3(), "fragment3");
                break;
            case R.id.btn4:
                btnC = 4;
                setColor(btnC);
                transaction.replace(R.id.center, new note_body4(), "fragment4");
                break;
            case R.id.btn5:
                btnC = 5;
                setColor(btnC);
                transaction.replace(R.id.center, new note_body5(), "fragment5");
                break;
            case R.id.btn6:
                btnC = 6;
                setColor(btnC);
                transaction.replace(R.id.center, new note_body6(), "fragment5");
                break;
            case R.id.Btn_edit:
                transaction.replace(R.id.center, new note_body1(), "fragment1a");
                break;
        }
        //呼叫commit讓變更生效。
        transaction.commit();
    }

    public void setColor(int btnC) {
        int countFragment = 6;
        Button btn[] = new Button[]{findViewById(R.id.btn1), findViewById(R.id.btn2),
                findViewById(R.id.btn3), findViewById(R.id.btn4), findViewById(R.id.btn5), findViewById(R.id.btn6)};

        for (int i = 0; i < countFragment; i++) {
            if (i == btnC - 1) {
                btn[i].setTextColor(Color.WHITE);
            } else {
                btn[i].setTextColor(Color.GRAY);
            }
        }
    }
}
