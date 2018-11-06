package com.jsaw.ibreast.note.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.jsaw.ibreast.CalendarIntentHelper;
import com.jsaw.ibreast.R;

import java.util.Calendar;

public class note_activity_add extends AppCompatActivity {
    private EditText edtAct;
    private EditText edtStartDate;
    private EditText edtEndDate;
    private EditText edtStartTime;
    private EditText edtEndTime;
    private EditText edtPlace;
    private EditText edtRemark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add);

        setViews();
    }

//    public void onClick(View v) {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog;
//        TimePickerDialog timePickerDialog;
//        switch (v.getId()) {
//            case R.id.edtStartDate:
//                datePickerDialog = new DatePickerDialog(note_activity_add.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                edtStartDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
//                            }
//                        }, year, month, day);
//                datePickerDialog.show();
//                break;
//            case R.id.edtEndDate:
//                datePickerDialog = new DatePickerDialog(note_activity_add.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                edtEndDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
//                            }
//                        }, year, month, day);
//                datePickerDialog.show();
//                break;
//            case R.id.edtStartTime:
//                timePickerDialog = new TimePickerDialog(note_activity_add.this, new TimePickerDialog.OnTimeSetListener() {
//                    @SuppressLint("DefaultLocale")
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        edtStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
//                    }
//                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
//                timePickerDialog.show();
//                break;
//            case R.id.edtEndTime:
//                timePickerDialog = new TimePickerDialog(note_activity_add.this, new TimePickerDialog.OnTimeSetListener() {
//                    @SuppressLint("DefaultLocale")
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        edtEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
//                    }
//                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
//                timePickerDialog.show();
//                break;
//        }
//    }

    View.OnTouchListener TouchListener = new View.OnTouchListener() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        TimePickerDialog timePickerDialog;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
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
            return false;
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
        edtStartDate.setOnTouchListener(TouchListener);
        edtEndDate.setOnTouchListener(TouchListener);
        edtStartTime.setOnTouchListener(TouchListener);
        edtEndTime.setOnTouchListener(TouchListener);
    }

    // 寫入行事曆
    private void createCalendar() {
        //建立事件開始時間
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 9, 19, 17, 50);
        //建立事件結束時間
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 9, 19, 19, 30);
        //建立 CalendarIntentHelper 實體
        CalendarIntentHelper calIntent = new CalendarIntentHelper();
        //設定值
        calIntent.setTitle("事件標題");
        calIntent.setDescription("事件內容描述");
        calIntent.setBeginTimeInMillis(beginTime.getTimeInMillis());
        calIntent.setEndTimeInMillis(endTime.getTimeInMillis());
        calIntent.setLocation("事件地點");
        //全部設定好後就能夠取得 Intent
        Intent intent = calIntent.getIntentAfterSetting();
        //送出意圖
        startActivity(intent);
    }
}
