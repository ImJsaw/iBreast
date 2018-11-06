package com.jsaw.ibreast.note.body;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.Calendar;


public class body2_add extends Fragment {
    private EditText edtDate;
    private EditText edtTemp;
    private EditText edtTime;
    private ImageButton btnCheck;
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private TextView txtWarn;

    private static class Record {
        public String date;
        public String time;
        public String temp;

        Record(String date, String time, String temp) {
            this.date = date;
            this.time = time;
            this.temp = temp;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        isProgressDialogShow = true;
        //connect time out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isProgressDialogShow) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_note_body2_add, container, false);
        txtWarn = view.findViewById(R.id.txtWarn);
        edtDate = view.findViewById(R.id.edtDate);
        edtTemp = view.findViewById(R.id.edtTemp);
        edtTime = view.findViewById(R.id.edtTime);
        btnCheck = view.findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(mBtnCheck);
        edtTemp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!edtTemp.getText().toString().isEmpty()) {
                    double wbcWarn = Double.valueOf(edtTemp.getText().toString());
                    if (wbcWarn >= 38) {
                        txtWarn.setVisibility(View.VISIBLE);
                    } else {
                        txtWarn.setVisibility(View.INVISIBLE);
                    }
                }
                return false;
            }
        });

        //自動帶入時間
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("DefaultLocale")
        String time = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        edtTime.setText(time);

        // 設定小日曆選擇時間
        ImageButton selectDate = view.findViewById(R.id.imgCal);
        selectDate.setOnClickListener(imgCalOnClick);

        isProgressDialogShow = false;
        progressDialog.dismiss();

        return view;

    }

    // 確認按鈕
    private View.OnClickListener mBtnCheck = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveData(edtDate.getText().toString(), edtTime.getText().toString(),
                    edtTemp.getText().toString());
            isProgressDialogShow = false;
            progressDialog.dismiss();
            Toast.makeText(getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
        }
    };

    //日曆按鈕
    private View.OnClickListener imgCalOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            edtDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                        }
                    }, year, month, day);
//                顯示從目前時間開始選
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    };

    // 儲存資料
    private void saveData(String date, String time, String temp) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = new Record(date, time, temp);
        if (!date.equals("")) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser users = auth.getCurrentUser();
                    String user = users.getUid();
                    String count = String.valueOf(dataSnapshot.child(user).child("體溫").getChildrenCount() + 1);
                    mDatabase.child(user).child("體溫").child(count).setValue(record);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(getContext(), "儲存失敗", Toast.LENGTH_SHORT).show();
        }
    }

}
