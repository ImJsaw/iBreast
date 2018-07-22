package com.jsaw.ibreast.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jsaw.ibreast.R;

import java.util.Objects;

public class note_treat2 extends AppCompatActivity {
    Button mBtnAdd;
    Button mBtn1;
    Button mBtn3;
    Button mBtn4;
    Button mBtn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_treat2);
        mBtnAdd = findViewById(R.id.Btn_edit);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat2.this, note_treat2_add.class));
            }
        });
        mBtn1 = findViewById(R.id.btn1);
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat2.this, note_treat1.class));
            }
        });
        mBtn3 = findViewById(R.id.btn3);
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat2.this, note_treat3.class));
            }
        });
        mBtn4 = findViewById(R.id.btn4);
        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat2.this, note_treat4.class));
            }
        });
        mBtn5 = findViewById(R.id.btn5);
        mBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat2.this, note_treat5.class));
            }
        });
    }
}
