package com.jsaw.ibreast.note.body;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.Calendar;


public class body3_add extends Fragment {
    private EditText edtDate;
    private EditText edtWbc;
    private EditText edtHb;
    private EditText edtPlt;
    private TextView txtWarn;
    private ImageButton btnCheck;
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;


    private static class Record {
        public String date;
        public String wbc;
        public String hb;
        public String plt;

        Record(String date, String wbc, String hb, String plt) {
            this.date = date;
            this.wbc = wbc;
            this.hb = hb;
            this.plt = plt;
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
        final View view = inflater.inflate(R.layout.activity_note_body3_add, container, false);
        txtWarn = view.findViewById(R.id.txtWarn);
        edtDate = view.findViewById(R.id.edtDate);
        edtWbc = view.findViewById(R.id.edtWbc);
        edtHb = view.findViewById(R.id.edtHb);
        edtPlt = view.findViewById(R.id.edtPlt);
        btnCheck = view.findViewById(R.id.btnCheack);
        btnCheck.setOnClickListener(mBtnCheck);
        edtWbc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!edtWbc.getText().toString().isEmpty()) {
                    int wbcWarn = Integer.valueOf(edtWbc.getText().toString());
                    if (wbcWarn <= 3000) {
                        txtWarn.setVisibility(View.VISIBLE);
                    } else {
                        txtWarn.setVisibility(View.INVISIBLE);
                    }
                }
                return false;
            }
        });
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
            saveData(edtDate.getText().toString(), edtWbc.getText().toString(),
                    edtHb.getText().toString(), edtPlt.getText().toString());
            isProgressDialogShow = false;
            progressDialog.dismiss();
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
    private void saveData(String date, String wbc, String hb, String plt) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = new Record(date, wbc, hb, plt);
        if (!date.equals("")) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser users = auth.getCurrentUser();
                    String user = users.getUid();
                    String count = String.valueOf(dataSnapshot.child(user).child("血液").getChildrenCount() + 1);
                    mDatabase.child(user).child("血液").child(count).setValue(record);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            isProgressDialogShow = false;
            progressDialog.dismiss();
            Toast.makeText(getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "儲存失敗", Toast.LENGTH_SHORT).show();
        }
    }

}
