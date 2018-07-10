package com.jsaw.ibreast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class move extends AppCompatActivity {
    ImageButton btn_exercise;
    ImageButton btn_rehab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        btn_exercise=  findViewById(R.id.btn_exercise);
        btn_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(move.this, open_movie.class).putExtra("URL",
                        "https://www.youtube.com/watch?v=CLzUTDBKFqY&index=1&list=PLcYwYvFNfjqY17T85PVKFTonogszd_Hha"));
            }
        });

        btn_rehab=  findViewById(R.id.btn_rehab);
        btn_rehab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(move.this, open_movie.class).putExtra("URL",
                        "https://www.youtube.com/watch?v=Zwkd4kKkX9Q&index=1&list=PLcYwYvFNfjqY9h9StS_A2sRNB_1BAtvMT"));
            }
        });
    }
}
