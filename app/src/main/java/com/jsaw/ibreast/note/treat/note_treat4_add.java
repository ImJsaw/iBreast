package com.jsaw.ibreast.note.treat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class note_treat4_add extends Fragment implements View.OnClickListener {
    private static final int[] IDS = new int[]{R.id.checkbox_listview, R.id.engtxt_listview, R.id.chitxt_listview};
    private static final String[] STRINGS = new String[]{"checkbox", "engName", "chiName"};
    private EditText edtStartDate;
    private EditText edtEndDate;
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;
    private List<String> item = new ArrayList<>();
    private ImageButton checkBtn;
    private EditText edtOther;
    private CheckBox ckbOther;
    private String message;

    private static class Record {
        public List<String> part;

        Record(List<String> part) {
            this.part = part;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_note_treat2_add, container, false);
        setDialog();
        edtStartDate = view.findViewById(R.id.edtStartDate);
        edtEndDate = view.findViewById(R.id.edtEndDate);
        checkBtn = view.findViewById(R.id.imgCheack);
        ckbOther = view.findViewById(R.id.ckbOther);
        edtOther = view.findViewById(R.id.edtOther);
        ckbOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ckbOther.isChecked()) {
                    item.add(edtOther.getText().toString());
                } else {
                    item.remove(edtOther);
                }
            }
        });

        ImageButton imgCalStart = view.findViewById(R.id.imgCalStart);
        ImageButton imgCalEnd = view.findViewById(R.id.imgCalEnd);
        imgCalStart.setOnClickListener(this);
        imgCalEnd.setOnClickListener(this);
        checkBtn.setOnClickListener(this);

        firebaseGetData(view);

        return view;
    }


    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        switch (view.getId()) {
            case R.id.imgCalStart:
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtStartDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.imgCalEnd:
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtEndDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.imgCheack:
                if (checkBlank()){
                    saveData();
                } else {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void firebaseGetData(final View view) {
        FirebaseDatabase.getInstance().getReference("treat_add").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> items = new ArrayList<>();
                ListView listView = view.findViewById(R.id.list_noteAdd);

                dataSnapshot = dataSnapshot.child("treat3_add");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("engName", data.child("engName").getValue().toString());
                    item.put("chiName", data.child("chiName").getValue().toString());
                    items.add(item);
                }
//                1. Context context 執行環境
//                2. List<? extends Map<String, ?>> data 帶入的資料
//                3. int resource Layout位置
//                4. String[] from data帶入資料的Key
//                5. int[] to Key的值要帶到哪個元件
                SimpleAdapter sa = new SimpleAdapter(getContext(), items, R.layout.note_list_view,
                        STRINGS, IDS) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        final HashMap<String, Object> map = (HashMap<String, Object>) this.getItem(position);
                        checkBox = v.findViewById(R.id.checkbox_listview);
                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vv) {
                                map.put("isSelect", ((CheckBox) vv).isChecked());
                                if (((CheckBox) vv).isChecked()) {
//                                    Toast.makeText(activity_note_my.this, "選中了" + map.get("item"), Toast.LENGTH_SHORT).show();
                                    item.add((String) map.get("engName"));
                                } else {
                                    item.remove(map.get("item"));
                                }
                            }
                        });
                        return v;
                    }
                };
                listView.setAdapter(sa);
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Save Data
    private void saveData() {
        setDialog();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                Record part_record = new Record(item);
                String count = String.valueOf(dataSnapshot.child(user).child("荷爾蒙").getChildrenCount() + 1);
                mDatabase.child(user).child("荷爾蒙").child(count).setValue(part_record);
                mDatabase.child(user).child("荷爾蒙").child(count).child("startDate").setValue(edtStartDate.getText().toString());
                mDatabase.child(user).child("荷爾蒙").child(count).child("endDate").setValue(edtEndDate.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();
        isProgressDialogShow = false;
        Toast.makeText(getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
    }

    private void setDialog() {
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

    // 是否有為填選欄位
    /**
     *
     *   @return true:沒有空白欄位
     */
    private boolean checkBlank() {
        if (edtStartDate.getText().toString().trim().equals("")) {
            message = "開始日期不可為空白";
            return false;
        } else if (edtEndDate.getText().toString().trim().equals("")) {
            message = "結束日期不可為空白";
            return false;
        } else if (item.size() == 0) {
            message = "請勾選部位";
            return false;
        } else {
            return true;
        }
    }
}


