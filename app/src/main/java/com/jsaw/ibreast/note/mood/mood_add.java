package com.jsaw.ibreast.note.mood;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class mood_add extends AppCompatActivity {
    private Integer score;
    private TextView txtScore;
    private String mydate;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_add);

        TextView edtDate = findViewById(R.id.edtDate);
        ImageButton imgResult = findViewById(R.id.imgResult);
        imgResult.setOnClickListener(mBtnResult);

        //自動帶入日期
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        mydate = sdf.format(date);
        edtDate.setText(mydate);
    }

    View.OnClickListener mBtnResult = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            score = getScore();
            if (score >= 0) {
                Intent intent = new Intent(mood_add.this, mood_result.class);
                intent.putExtra("date", mydate);
                intent.putExtra("score", score);
                startActivity(intent);
            } else{
                Toast.makeText(mood_add.this, "尚未填寫完畢", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 取得分數
    public int getScore() {
        RadioGroup radioGroup1 = findViewById(R.id.radioGroup2);
        RadioGroup radioGroup2 = findViewById(R.id.radioGroup3);
        RadioGroup radioGroup3 = findViewById(R.id.radioGroup4);
        RadioGroup radioGroup4 = findViewById(R.id.radioGroup5);
        RadioGroup radioGroup5 = findViewById(R.id.radioGroup6);

        int selectId1 = (radioGroup1.getCheckedRadioButtonId()) % 5 - 1;
        int selectId2 = (radioGroup2.getCheckedRadioButtonId()) % 5 - 1;
        int selectId3 = (radioGroup3.getCheckedRadioButtonId()) % 5 - 1;
        int selectId4 = (radioGroup4.getCheckedRadioButtonId()) % 5 - 1;
        int selectId5 = (radioGroup5.getCheckedRadioButtonId()) % 5 - 1;

        // -1為第五的選項
        // -2為空選項
        if (selectId1 == -1) {
            selectId1 = 4;
        } else if (selectId1 == -2) {
            return -1;
        }
        if (selectId2 == -1) {
            selectId2 = 4;
        } else if (selectId2 == -2) {
            return -1;
        }
        if (selectId3 == -1) {
            selectId3 = 4;
        } else if (selectId3 == -2) {
            return -1;
        }
        if (selectId4 == -1) {
            selectId4 = 4;
        } else if (selectId4 == -2) {
            return -1;
        }
        if (selectId5 == -1) {
            selectId5 = 4;
        } else if (selectId5 == -2) {
            return -1;
        }

        return selectId1 + selectId2 + selectId3 + selectId4 + selectId5;
    }
}
