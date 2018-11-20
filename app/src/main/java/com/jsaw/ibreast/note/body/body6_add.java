package com.jsaw.ibreast.note.body;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class body6_add extends Fragment implements View.OnClickListener {
    private EditText edtDate;
    private List<String> checkBoxStrList = new LinkedList<>();

    private static class Record {
        public String date;
        public List<String> record;

        Record(String date, List<String> record) {
            this.date = date;
            this.record = record;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_note_body6_add, container, false);
        setViews(view);

        //自動帶日期
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        edtDate.setText(time);

        // 設定小日曆選擇時間
        ImageButton selectDate = view.findViewById(R.id.imgCal);
        selectDate.setOnClickListener(imgCalOnClick);

        return view;

    }

    // 確認儲存按鈕
    private View.OnClickListener mBtnStore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveData(edtDate.getText().toString());
            startActivity(new Intent().setClass(getContext(), note_body.class));
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
    private void saveData(String date) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = new Record(date, checkBoxStrList);
        if (!date.isEmpty() && checkBoxStrList.size() > 0) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser users = auth.getCurrentUser();
                    String user = users.getUid();
                    String count = String.valueOf(dataSnapshot.child(user).child("其他").getChildrenCount() + 1);
                    mDatabase.child(user).child("其他").child(count).setValue(record);
                    startActivity(new Intent().setClass(getActivity(), note_body.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            Toast.makeText(getContext(), "儲存成功", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "請填選日期或至少勾選一項", Toast.LENGTH_SHORT).show();
        }
    }

    // 取得打勾的 CheckBox 字串
    public void onClick(View v) {
        CheckBox checkBox = v.findViewById(v.getId());
        String checkBoxText = checkBox.getText().toString();
        if (checkBox.isChecked()) {
            checkBoxStrList.add(checkBoxText);
        } else {
            checkBoxStrList.remove(checkBoxText);
        }
    }

    private void setViews(View view) {
        for (int i = 1 ; i <= 11 ; i++){
            int resID = getResources().getIdentifier("checkBox" + i, "id", getActivity().getPackageName());
            CheckBox checkBox = view.findViewById(resID);
            checkBox.setOnClickListener(this);
        }
        edtDate = view.findViewById(R.id.edtDate);

        ImageButton btnStore = view.findViewById(R.id.btn_store);
        btnStore.setOnClickListener(mBtnStore);
    }
}
