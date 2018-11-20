package com.jsaw.ibreast.note.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.CalendarIntentHelper;
import com.jsaw.ibreast.R;
import com.jsaw.ibreast.note.body.body2_add;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class note_activity_add extends AppCompatActivity {
    private EditText edtAct;
    private EditText edtStartDate;
    private EditText edtEndDate;
    private EditText edtStartTime;
    private EditText edtEndTime;
    private EditText edtPlace;
    private EditText edtRemark;

    private static class Record {
        public Map<String, String> record;

        Record(Map<String, String> record) {
            this.record = record;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add);

        setViews();
    }

//    // 確認按鈕
//    private View.OnClickListener mBtnCheck = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Map<String, String> data = new HashMap<>();
//            data.put("event", edtAct.getText().toString());
//            data.put("startDate", edtStartDate.getText().toString());
//            data.put("endDate", edtEndDate.getText().toString());
//            data.put("startTime", edtStartTime.getText().toString());
//            data.put("endTime", edtEndTime.getText().toString());
//            data.put("place", edtPlace.getText().toString());
//            data.put("remark", edtRemark.getText().toString());
//            saveData(data);
//        }
//    };
//
//    //加入行事曆按鈕
//    private View.OnClickListener mBtnCalendar = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            createCalendar(data);
//        }
//    };

    public void onClick(View v){
        Map<String, String> data = new HashMap<>();
        data.put("event", edtAct.getText().toString());
        data.put("startDate", edtStartDate.getText().toString());
        data.put("endDate", edtEndDate.getText().toString());
        data.put("startTime", edtStartTime.getText().toString());
        data.put("endTime", edtEndTime.getText().toString());
        data.put("place", edtPlace.getText().toString());
        data.put("remark", edtRemark.getText().toString());
        switch (v.getId()){
            case R.id.btnCheck:
                saveData(data);
                break;
            case R.id.btnCalendar:
                createCalendar(data);
                break;
        }
    }

    private void saveData(Map<String, String> data) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = new Record(data);
        if (isDataEmpty(data)) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser users = auth.getCurrentUser();
                    String user = users.getUid();
                    String count = String.valueOf(dataSnapshot.child(user).child("活動").getChildrenCount() + 1);
                    mDatabase.child(user).child("活動").child(count).setValue(record);
                    Toast.makeText(note_activity_add.this, "儲存成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent().setClass(note_activity_add.this, note_activity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } else {
            Toast.makeText(note_activity_add.this, "請確實填寫空格", Toast.LENGTH_SHORT).show();
        }
    }

    View.OnFocusChangeListener TouchListener = new View.OnFocusChangeListener() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        TimePickerDialog timePickerDialog;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                switch (v.getId()) {
                    case R.id.edtStartDate:
                        datePickerDialog = new DatePickerDialog(note_activity_add.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        edtStartDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                                    }
                                }, year, month, day);
                        datePickerDialog.show();
                        break;
                    case R.id.edtEndDate:
                        datePickerDialog = new DatePickerDialog(note_activity_add.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        edtEndDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                                    }
                                }, year, month, day);
                        datePickerDialog.show();
                        break;
                    case R.id.edtStartTime:
                        timePickerDialog = new TimePickerDialog(note_activity_add.this, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edtStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        timePickerDialog.show();
                        break;
                    case R.id.edtEndTime:
                        timePickerDialog = new TimePickerDialog(note_activity_add.this, new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edtEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        timePickerDialog.show();
                        break;
                }
            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private void setViews() {
        edtAct = findViewById(R.id.edtAct);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtEndTime = findViewById(R.id.edtEndTime);
        edtPlace = findViewById(R.id.edtPlace);
        edtRemark = findViewById(R.id.edtRemark);
//        ImageButton btnCheck = findViewById(R.id.btnCheck);
//        Button btnCalendar = findViewById(R.id.btnCalendar);
//        btnCheck.setOnClickListener(mBtnCheck);
//        btnCalendar.setOnClickListener(mBtnCalendar);
        edtStartDate.setOnFocusChangeListener(TouchListener);
        edtEndDate.setOnFocusChangeListener(TouchListener);
        edtStartTime.setOnFocusChangeListener(TouchListener);
        edtEndTime.setOnFocusChangeListener(TouchListener);
    }

    // 寫入行事曆
    private void createCalendar(Map<String, String> data) {
        //建立事件開始時間
        Calendar beginTime = Calendar.getInstance();
        String date = data.get("startDate");
        String time = data.get("startTime");
        String[] aryDate = date.split("/");
        String[] aryTime = time.split(":");
        beginTime.set(Integer.valueOf(aryDate[0]), Integer.valueOf(aryDate[1]) - 1,
                Integer.valueOf(aryDate[2]), Integer.valueOf(aryTime[0]), Integer.valueOf(aryTime[1]));
        //建立事件結束時間
        Calendar endTime = Calendar.getInstance();
        date = data.get("endDate");
        time = data.get("endTime");
        aryDate = date.split("/");
        aryTime = time.split(":");
        endTime.set(Integer.valueOf(aryDate[0]), Integer.valueOf(aryDate[1]) - 1,
                Integer.valueOf(aryDate[2]), Integer.valueOf(aryTime[0]), Integer.valueOf(aryTime[1]));
        //建立 CalendarIntentHelper 實體
        CalendarIntentHelper calIntent = new CalendarIntentHelper();
        //設定值
        calIntent.setTitle(data.get("event"));
        calIntent.setDescription(data.get("remark"));
        calIntent.setBeginTimeInMillis(beginTime.getTimeInMillis());
        calIntent.setEndTimeInMillis(endTime.getTimeInMillis());
        calIntent.setLocation(data.get("place"));
        //全部設定好後就能夠取得 Intent
        Intent intent = calIntent.getIntentAfterSetting();
        //送出意圖
        startActivity(intent);
    }

    // 判斷是否有位填資料
    private static boolean isDataEmpty(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
