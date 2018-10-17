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

public class mood_result extends AppCompatActivity{
    private TextView txtScore;
    private TextView txtResult;
    private EditText edtWords;
    private ImageButton btnSave;
    private Integer score;
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
        setContentView(R.layout.activity_mood_result);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", -1);
        mydate = intent.getStringExtra("date");

        txtScore = findViewById(R.id.txtScore);
        txtResult = findViewById(R.id.txtResult);
        edtWords = findViewById(R.id.edtWords);
        btnSave = findViewById(R.id.btnSave);

        txtScore.setText(score.toString());
        txtResult.setText(setResultMessage(score));

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                EditText edtWords = findViewById(R.id.edtWords);
                saveData(mydate, score, edtWords.getText().toString());
                break;

        }
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
        if (!date.equals("") & score != -1) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String count = String.valueOf(dataSnapshot.child("心情").getChildrenCount() + 1);
                    mDatabase.child("心情").child(count).setValue(record);
                    Toast.makeText(mood_result.this, "儲存成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent().setClass(mood_result.this, mood.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(this, "儲存失敗", Toast.LENGTH_SHORT).show();
        }
    }

    private String setResultMessage(Integer score) {
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
}
