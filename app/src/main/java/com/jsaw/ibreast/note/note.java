package com.jsaw.ibreast.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jsaw.ibreast.R;
import com.jsaw.ibreast.link.link;
import com.jsaw.ibreast.main;

public class note extends AppCompatActivity{
    Button mBtnMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mBtnMy = findViewById(R.id.Btn_my);

        mBtnMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_my.class));
            }
        });
    }
}
