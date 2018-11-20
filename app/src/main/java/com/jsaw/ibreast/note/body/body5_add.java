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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class body5_add extends Fragment implements View.OnClickListener {
    private EditText edtDate;
    private List<EditText> edtList = new LinkedList<>();
    private EditText chosenView;
    private EditText edtHurt;

    private static class Record {
        public String date;
        public List<String> record = new LinkedList<>();
        Record(String date, List<EditText> edtList, EditText edtHurt) {
            for (EditText editText : edtList){
                record.add(editText.getText().toString());
            }
            record.add(edtHurt.getText().toString());
            this.date = date;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_note_body5_add, container, false);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:  // 除了疼痛之外的症狀
                if (resultCode == Activity.RESULT_OK) {
                    String level = data.getStringExtra("level");
                    chosenView.setText(level);
                }
                break;
            case 2:  // 疼痛指數
                if (resultCode == Activity.RESULT_OK) {
                    String score = data.getStringExtra("score");
                    edtHurt.setText(score);
                }
                break;
        }
    }

    // 確認儲存按鈕
    private View.OnClickListener mBtnCheck = new View.OnClickListener() {
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

    // 疼痛分數按鈕
    private View.OnClickListener mBtnHurt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), body5_hurt.class);
            startActivityForResult(intent, 2);
        }
    };

    // 儲存資料
    private void saveData(String date) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = new Record(date, edtList, edtHurt);
        if (isAllOptionsSelected()) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser users = auth.getCurrentUser();
                    String user = users.getUid();
                    String count = String.valueOf(dataSnapshot.child(user).child("症狀").getChildrenCount() + 1);
                    mDatabase.child(user).child("症狀").child(count).setValue(record);
                    startActivity(new Intent().setClass(getActivity(), note_body.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            Toast.makeText(getContext(), "儲存成功", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "選項尚未選擇完畢", Toast.LENGTH_SHORT).show();
        }
    }

    // 判斷是否有未選擇選項
    private boolean isAllOptionsSelected(){
        final String notChose = "尚未選擇";
        for (EditText editText : edtList){
            if (editText.getText().toString().equals(notChose)){
                return false;
            }
        }

        return true;
    }

    // 症狀分級選擇
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), body5_record.class);
        switch (v.getId()) {
            case R.id.edtOne:
                intent.putExtra("sign", "噁心");
                chosenView = edtList.get(0);
                break;
            case R.id.edtTwo:
                intent.putExtra("sign", "嘔吐");
                chosenView = edtList.get(1);
                break;
            case R.id.edtThree:
                intent.putExtra("sign", "腹瀉");
                chosenView = edtList.get(2);
                break;
            case R.id.edtFour:
                intent.putExtra("sign", "便祕");
                chosenView = edtList.get(3);
                break;
            case R.id.edtFive:
                intent.putExtra("sign", "掉髮");
                chosenView = edtList.get(4);
                break;
            case R.id.edtSix:
                intent.putExtra("sign", "疲倦");
                chosenView = edtList.get(5);
                break;
            case R.id.edtSeven:
                intent.putExtra("sign", "食慾不振");
                chosenView = edtList.get(6);
                break;
            case R.id.edtEight:
                intent.putExtra("sign", "口腔炎");
                chosenView = edtList.get(7);
                break;
            case R.id.edtNine:
                intent.putExtra("sign", "手足症候群");
                chosenView = edtList.get(8);
                break;
        }
        startActivityForResult(intent, 1);
    }

    private void setViews(View view) {
        edtList.add((EditText) view.findViewById(R.id.edtOne));
        edtList.add((EditText) view.findViewById(R.id.edtTwo));
        edtList.add((EditText) view.findViewById(R.id.edtThree));
        edtList.add((EditText) view.findViewById(R.id.edtFour));
        edtList.add((EditText) view.findViewById(R.id.edtFive));
        edtList.add((EditText) view.findViewById(R.id.edtSix));
        edtList.add((EditText) view.findViewById(R.id.edtSeven));
        edtList.add((EditText) view.findViewById(R.id.edtEight));
        edtList.add((EditText) view.findViewById(R.id.edtNine));
        for (EditText edt : edtList){
            edt.setOnClickListener(this);
        }
        edtHurt = view.findViewById(R.id.edtHurt);
        edtHurt.setOnClickListener(mBtnHurt);
        edtDate = view.findViewById(R.id.edtDate);

        ImageButton btnCheck = view.findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(mBtnCheck);
    }
}
