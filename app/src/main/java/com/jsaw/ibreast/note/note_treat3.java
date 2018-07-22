package com.jsaw.ibreast.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jsaw.ibreast.R;

public class note_treat3 extends AppCompatActivity {
    Button mBtnAdd;
    Button mBtn2;
    Button mBtn1;
    Button mBtn4;
    Button mBtn5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_treat3);


        mBtn2 = findViewById(R.id.btn2);
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat3.this, note_treat3.class));
            }
        });
        mBtn1 = findViewById(R.id.btn1);
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat3.this, note_treat1.class));
            }
        });
        mBtn4 = findViewById(R.id.btn4);
        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat3.this, note_treat4.class));
            }
        });
        mBtn5 = findViewById(R.id.btn5);
        mBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note_treat3.this, note_treat5.class));
            }
        });
    }
}
