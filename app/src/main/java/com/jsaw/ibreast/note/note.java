package com.jsaw.ibreast.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jsaw.ibreast.R;
import com.jsaw.ibreast.link.link;
import com.jsaw.ibreast.main;

import java.util.Objects;

public class note extends AppCompatActivity {
    Button mBtnMy;
    Button mBtnTreat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        mBtnMy = findViewById(R.id.Btn_my);
        mBtnMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_my.class));
            }
        });

        mBtnTreat = findViewById(R.id.Btn_treat);
        mBtnTreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_treat_main.class));
            }
        });
    }
}
