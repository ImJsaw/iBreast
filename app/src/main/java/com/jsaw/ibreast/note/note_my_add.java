package com.jsaw.ibreast.note;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.util.Map;
import java.util.Objects;

public class note_my_add extends AppCompatActivity {
    private EditText edtBirth;
    private EditText edtDate;
    private EditText edtHeight;
    private CheckBox checkBox;
    private static final int[] IDS = new int[]{R.id.checkbox_listview, R.id.chitxt_listview};
    private static final String[] STRINGS = new String[]{"checkbox", "chiName"};
    private List<String> method = new ArrayList<>();
    private List<String> program = new ArrayList<>();
    private String message;
    private Map<String, String> myMap = new HashMap<>();
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;

    private static class Record {
        public List<String> record;

        Record(List<String> record) {
            this.record = record;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_my_add);

        getMethodData();
        getProgramData();

        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(mBtnSave);
    }

    // 設定小日曆選擇時間
    public void onImgClick(final View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog;

        switch (view.getId()) {
            case R.id.imgBirth:
                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtBirth.setText(year - 1911 + "年" + (month + 1) + "月");
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.imgCal:
                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtDate.setText(year - 1911 + "年" + (month + 1) + "月");
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
        }
    }

    // 儲存按鈕
    View.OnClickListener mBtnSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 儲存資料
            if (checkBlank()) {
                setData();
                setDialog();
                saveData();
                startActivity(new Intent().setClass(note_my_add.this, note_my.class)); //儲存完回到 activity_note_my
            } else {    // 欄位有空白不能儲存
                Toast.makeText(note_my_add.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 取得術式資料
    private void getMethodData() {
        setDialog();
        FirebaseDatabase.getInstance().getReference("my").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> method_items = new ArrayList<>();
                ListView listMethod = findViewById(R.id.list_method);
                dataSnapshot = dataSnapshot.child("method");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("chiName", data.getValue().toString());
                    method_items.add(item);
                }
                SimpleAdapter sa = new SimpleAdapter(note_my_add.this, method_items, R.layout.note_list_view,
                        STRINGS, IDS) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        final HashMap<String, Object> myMap = (HashMap<String, Object>) this.getItem(position);
                        checkBox = v.findViewById(R.id.checkbox_listview);
                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vv) {
                                myMap.put("isSelect", ((CheckBox) vv).isChecked());
                                if (((CheckBox) vv).isChecked()) {
//                                    Toast.makeText(activity_note_my_old.this, "選中了" + myMap.get("chiName"), Toast.LENGTH_SHORT).show();
                                    method.add((String) myMap.get("chiName"));
                                } else {
                                    method.remove(myMap.get("chiName"));
                                }
                            }
                        });
                        return v;
                    }
                };
                listMethod.setAdapter(sa);
                progressDialog.dismiss();
                isProgressDialogShow = false;
                setListViewHeightBasedOnChildren(listMethod);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // 取得療程計畫資料
    private void getProgramData() {
        setDialog();
        FirebaseDatabase.getInstance().getReference("my").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> program_items = new ArrayList<>();
                ListView listProgram = findViewById(R.id.list_program);
                dataSnapshot = dataSnapshot.child("program");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("chiName", data.getValue().toString());
                    program_items.add(item);
                }

                SimpleAdapter sa = new SimpleAdapter(note_my_add.this, program_items, R.layout.note_list_view,
                        STRINGS, IDS) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        final HashMap<String, Object> myMap = (HashMap<String, Object>) this.getItem(position);
                        checkBox = v.findViewById(R.id.checkbox_listview);
                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vv) {
                                myMap.put("isSelect", ((CheckBox) vv).isChecked());
                                if (((CheckBox) vv).isChecked()) {
//                                    Toast.makeText(activity_note_my_old.this, "選中了" + myMap.get("chiName"), Toast.LENGTH_SHORT).show();
                                    program.add((String) myMap.get("chiName"));
                                } else {
                                    program.remove(myMap.get("chiName"));
                                }
                            }
                        });
                        return v;
                    }
                };
                listProgram.setAdapter(sa);
                progressDialog.dismiss();
                isProgressDialogShow = false;
                setListViewHeightBasedOnChildren(listProgram);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setDialog() {
        progressDialog = new ProgressDialog(note_my_add.this);
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        isProgressDialogShow = true;
        //connect time out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isProgressDialogShow) {
                    progressDialog.dismiss();
                    Toast.makeText(note_my_add.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
    }

    private void saveData() {
        final DatabaseReference mSavadata = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser users = auth.getCurrentUser();
        String user = users.getUid();
        for (Map.Entry<String, String> entry : myMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            mSavadata.child(user).child(key).setValue(value);
        }
        Record method_record = new Record(method);
        Record program_record = new Record(program);
        mSavadata.child(user).child("surgeryMethod").setValue(method_record);
        mSavadata.child(user).child("program").setValue(program_record);

        progressDialog.dismiss();
        isProgressDialogShow = false;
//        Toast.makeText(activity_note_my_add.this, "儲存成功", Toast.LENGTH_SHORT).show();
    }

    //將儲存的資料製作成 Map
    private void setData() {
        RadioButton rdbYes = findViewById(R.id.rdbYes);
        Spinner spCell = findViewById(R.id.spinnerCell);
        Spinner spEr = findViewById(R.id.spinnerEr);
        Spinner spPr = findViewById(R.id.spinnerPr);
        Spinner spHer = findViewById(R.id.spinnerHer);
        Spinner spFish = findViewById(R.id.spinnerFish);
        edtBirth = findViewById(R.id.edtBirth);
        edtDate = findViewById(R.id.edtDate);
        edtHeight = findViewById(R.id.edtHeight);

        myMap.put("birth", edtBirth.getText().toString());
        myMap.put("height", edtHeight.getText().toString());
        myMap.put("stopBleed", rdbYes.isChecked() ? "是" : "否");
        myMap.put("surgeryDate", edtDate.getText().toString());
        myMap.put("cell", spCell.getSelectedItem().toString());
        myMap.put("er", spEr.getSelectedItem().toString());
        myMap.put("pr", spPr.getSelectedItem().toString());
        myMap.put("her", spHer.getSelectedItem().toString());
        myMap.put("fish", spFish.getSelectedItem().toString());
    }

    /**
     * 是否有為填選欄位
     *
     * @return true:沒有空白欄位
     */
    private boolean checkBlank() {
        if (edtBirth.getText().toString().trim().equals("")) {
            message = "生日欄位不可為空白";
            return false;
        } else if (edtDate.getText().toString().trim().equals("")) {
            message = "手術年月欄位不可為空白";
            return false;
        } else if (edtHeight.getText().toString().trim().equals("")) {
            message = "身高欄位不可為空白";
            return false;
        } else if (method.size() == 0) {
            message = "請勾選述式";
            return false;
        } else if (program.size() == 0) {
            message = "請勾選療程計畫";
            return false;
        } else {
            return true;
        }
    }

    /**
     * scrollView 中嵌listView造成View高度異常，根據子View高度重置listVIew高度。
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
