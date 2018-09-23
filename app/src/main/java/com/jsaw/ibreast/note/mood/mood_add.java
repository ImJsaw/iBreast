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

    private static class Record {
        public String date;
        public int score;
        public String words;

        Record(String date, int score, String words) {
            this.date = date;
            this.score = score;
            this.words = words;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_add);

        TextView edtDate = findViewById(R.id.edtDate);

        //自動帶入日期
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        mydate = sdf.format(date);
        edtDate.setText(mydate);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgResult:
                score = getScore();
                if (score >= 0) {
                    setContentView(R.layout.activity_mood_result);
                    mood_result();
                } else{
                    Toast.makeText(mood_add.this, "選項請填完", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSave:
                EditText edtWords = findViewById(R.id.edtWords);
                saveData(mydate, score, edtWords.getText().toString());
                break;

        }
    }

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

    // 結果 message
    private String resultMessage(Integer score) {
        String message;
        if (score >= 0 && score <= 5) {
            message = "為一般正常範圍，表示身心適應狀況良好。";
            txtScore.setTextColor(Color.GREEN);
        } else if (score >= 6 && score <= 9) {
            message = "輕度情緒困擾，建議找家人或朋友談談，抒發情緒。";
            txtScore.setTextColor(Color.YELLOW);
        } else if (score >= 10 && score <= 14) {
            message = "中度情緒困擾，建議尋求紓壓管道或接受心理專業諮詢。";
            txtScore.setTextColor(0xFFFF7F00); //橘色
        } else if (score >= 15) {
            message = "重度情緒困擾，建議諮詢精神科醫師接受進一步評估。";
            txtScore.setTextColor(Color.RED);
        } else {
            return "";
        }
        return message;
    }

    private void mood_result() {
        txtScore = findViewById(R.id.txtScore);
        TextView txtResult = findViewById(R.id.txtResult);
        txtScore.setText(String.valueOf(score));
        txtResult.setText(resultMessage(score));
    }

    // 儲存資料
    private void saveData(String date, int score, String words) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser users = auth.getCurrentUser();
        String user = users.getUid();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user);
        final Record record = new Record(date, score, words);
        if (!date.equals("")) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String count = String.valueOf(dataSnapshot.child("心情").getChildrenCount() + 1);
                    mDatabase.child("心情").child(count).setValue(record);
                    Toast.makeText(mood_add.this, "儲存成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(this, "儲存失敗", Toast.LENGTH_SHORT).show();
        }
    }
}
