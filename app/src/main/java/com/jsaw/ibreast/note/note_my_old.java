//package com.jsaw.ibreast.note;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.SimpleAdapter;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.jsaw.ibreast.R;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//public class note_my_old extends AppCompatActivity {
//    private EditText edtBirth;
//    private EditText edtDate;
//    private EditText edtHeight;
//    private CheckBox checkBox;
//    private static final int[] IDS = new int[]{R.id.checkbox_listview, R.id.chitxt_listview};
//    private static final String[] STRINGS = new String[]{"checkbox", "chiName"};
//    private Boolean isProgressDialogShow = false;
//    private ProgressDialog progressDialog;
//    private List<String> method = new ArrayList<>();
//    private List<String> program = new ArrayList<>();
//    private String message;
//    private Map<String, String> map = new HashMap<>();
//
//    private static class Record {
//
//        public List<String> record;
//
//        Record(List<String> record) {
//            this.record = record;
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_note_my_old);
//        readData();
//    }
//
//    // 設定小日曆選擇時間
//    public void onImgClick(final View view) {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog;
//
//        switch (view.getId()) {
//            case R.id.imgBirth:
//                datePickerDialog = new DatePickerDialog(this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                edtBirth.setText(year - 1911 + "年" + (month + 1) + "月");
//                            }
//                        }, year, month, day);
//                datePickerDialog.show();
//                break;
//            case R.id.imgCal:
//                datePickerDialog = new DatePickerDialog(this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                edtDate.setText(year - 1911 + "年" + (month + 1) + "月");
//                            }
//                        }, year, month, day);
//                datePickerDialog.show();
//                break;
//        }
//    }
//
//    public void myClick(View v) {
//        switch (v.getId()) {
//            case R.id.Btn_Info:
//                if (checkBlankOppo()) { //沒有空白欄位
//                    setContentView(R.layout.activity_note_my_old);
//                    readData();
//                } else {
//                    Leave();
//                }
//                break;
//            case R.id.Btn_edit:
//                setContentView(R.layout.activity_note_my_old_add);
//                onEdit();
//                break;
//            case R.id.Btn_save:
//                //儲存資料
//                if (checkBlank()) {
//                    setData();
//                    setDialog();
//                    saveData();
//                    setContentView(R.layout.activity_note_my_old); //儲存完回到 activity_note_my_old
//                    readData();
//                } else {    // 欄位有空白不能儲存
//                    Toast.makeText(note_my_old.this, message, Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
//
//    public void onEdit() {
//        edtBirth = findViewById(R.id.edtBirth);
//        edtDate = findViewById(R.id.edtDate);
//        edtHeight = findViewById(R.id.edtHeight);
//        setDialog();
//        getMethodData();
//        getProgramData();
//
//    }
//
//    // 取得術式資料
//    private void getMethodData() {
//        FirebaseDatabase.getInstance().getReference("my").addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<HashMap<String, Object>> method_items = new ArrayList<>();
//                ListView listMethod = findViewById(R.id.list_method);
//                dataSnapshot = dataSnapshot.child("method");
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    HashMap<String, Object> item = new HashMap<>();
//                    item.put("chiName", data.getValue().toString());
//                    method_items.add(item);
//                }
//                SimpleAdapter sa = new SimpleAdapter(note_my_old.this, method_items, R.layout.note_list_view,
//                        STRINGS, IDS) {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        View v = super.getView(position, convertView, parent);
//                        final HashMap<String, Object> map = (HashMap<String, Object>) this.getItem(position);
//                        checkBox = v.findViewById(R.id.checkbox_listview);
//                        checkBox.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View vv) {
//                                map.put("isSelect", ((CheckBox) vv).isChecked());
//                                if (((CheckBox) vv).isChecked()) {
////                                    Toast.makeText(activity_note_my_old.this, "選中了" + map.get("chiName"), Toast.LENGTH_SHORT).show();
//                                    method.add((String) map.get("chiName"));
//                                } else {
//                                    method.remove(map.get("chiName"));
//                                }
//                            }
//                        });
//                        return v;
//                    }
//                };
//                listMethod.setAdapter(sa);
//                progressDialog.dismiss();
//                isProgressDialogShow = false;
//                setListViewHeightBasedOnChildren(listMethod);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }
//
//    // 取得療程計畫資料
//    private void getProgramData() {
//        FirebaseDatabase.getInstance().getReference("my").addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<HashMap<String, Object>> program_items = new ArrayList<>();
//                ListView listProgram = findViewById(R.id.list_program);
//                dataSnapshot = dataSnapshot.child("program");
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    HashMap<String, Object> item = new HashMap<>();
//                    item.put("chiName", data.getValue().toString());
//                    program_items.add(item);
//                }
//
//                SimpleAdapter sa = new SimpleAdapter(note_my_old.this, program_items, R.layout.note_list_view,
//                        STRINGS, IDS) {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        View v = super.getView(position, convertView, parent);
//                        final HashMap<String, Object> map = (HashMap<String, Object>) this.getItem(position);
//                        checkBox = v.findViewById(R.id.checkbox_listview);
//                        checkBox.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View vv) {
//                                map.put("isSelect", ((CheckBox) vv).isChecked());
//                                if (((CheckBox) vv).isChecked()) {
////                                    Toast.makeText(activity_note_my_old.this, "選中了" + map.get("chiName"), Toast.LENGTH_SHORT).show();
//                                    program.add((String) map.get("chiName"));
//                                } else {
//                                    program.remove(map.get("chiName"));
//                                }
//                            }
//                        });
//                        return v;
//                    }
//                };
//                listProgram.setAdapter(sa);
//                progressDialog.dismiss();
//                isProgressDialogShow = false;
//                setListViewHeightBasedOnChildren(listProgram);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }
//
//    private void setDialog() {
//        progressDialog = new ProgressDialog(note_my_old.this);
//        progressDialog.setMessage("處理中,請稍候...");
//        progressDialog.show();
//        isProgressDialogShow = true;
//        //connect time out
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (isProgressDialogShow) {
//                    progressDialog.dismiss();
//                    Toast.makeText(note_my_old.this, "連線逾時", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, 5000);
//    }
//
//    private void saveData() {
//        final DatabaseReference mSavadata = FirebaseDatabase.getInstance().getReference("Users");
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser users = auth.getCurrentUser();
//        String user = users.getUid();
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            mSavadata.child(user).child(key).setValue(value);
//        }
//        Record method_record = new Record(method);
//        Record program_record = new Record(program);
//        mSavadata.child(user).child("surgeryMethod").setValue(method_record);
//        mSavadata.child(user).child("program").setValue(program_record);
//
//        progressDialog.dismiss();
//        isProgressDialogShow = false;
////        Toast.makeText(activity_note_my_old.this, "儲存成功", Toast.LENGTH_SHORT).show();
//    }
//
//    //將儲存的資料製作成 Map
//    private void setData() {
//        RadioButton rdbYes = findViewById(R.id.rdbYes);
//        Spinner spCell = findViewById(R.id.spinnerCell);
//        Spinner spEr = findViewById(R.id.spinnerEr);
//        Spinner spPr = findViewById(R.id.spinnerPr);
//        Spinner spHer = findViewById(R.id.spinnerHer);
//        Spinner spFish = findViewById(R.id.spinnerFish);
//
//
//        map.put("birth", edtBirth.getText().toString());
//        map.put("height", edtHeight.getText().toString());
//        map.put("stopBleed", rdbYes.isChecked() ? "是" : "否");
//        map.put("surgeryDate", edtDate.getText().toString());
//        map.put("cell", spCell.getSelectedItem().toString());
//        map.put("er", spEr.getSelectedItem().toString());
//        map.put("pr", spPr.getSelectedItem().toString());
//        map.put("her", spHer.getSelectedItem().toString());
//        map.put("fish", spFish.getSelectedItem().toString());
//
//    }
//
//
//
//    /**
//     *  是否有為填選欄位
//     * @return true:沒有空白欄位
//     */
//    private boolean checkBlank() {
//        if (edtBirth.getText().toString().trim().equals("")) {
//            message = "生日欄位不可為空白";
//            return false;
//        } else if (edtDate.getText().toString().trim().equals("")) {
//            message = "手術年月欄位不可為空白";
//            return false;
//        } else if (edtHeight.getText().toString().trim().equals("")) {
//            message = "身高欄位不可為空白";
//            return false;
//        } else if (method.size() == 0) {
//            message = "請勾選述式";
//            return false;
//        } else if (program.size() == 0) {
//            message = "請勾選療程計畫";
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private boolean checkBlankOppo() {
//        if (!edtBirth.getText().toString().trim().equals("")) {
//            return false;
//        } else if (!edtDate.getText().toString().trim().equals("")) {
//            return false;
//        } else if (!edtHeight.getText().toString().trim().equals("")) {
//            return false;
//        } else if (method.size() != 0) {
//            return false;
//        } else if (program.size() != 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    //詢問是否儲存 Dialog
//    private void Leave() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setMessage("是否未儲存離開?")
//                .setPositiveButton("否", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 右方按鈕方法
//                        // 繼續編輯
//                    }
//                })
//                .setNegativeButton("是", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 左方按鈕方法
//                        setContentView(R.layout.activity_note_my_old);
//                        readData();
//                    }
//                });
//        AlertDialog about_dialog = builder.create();
//        about_dialog.show();
//    }
//
//    //讀取資料
//    private void readData() {
//        setDialog();
//        final Map<String, TextView> myTextView = new HashMap<>();
//
//
//        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                myTextView.put("birth", (TextView) findViewById(R.id.txtInputBirth));
//                myTextView.put("height", (TextView) findViewById(R.id.txtInputHeight));
//                myTextView.put("stopBleed", (TextView) findViewById(R.id.txtInputStopBleeding));
//                myTextView.put("surgeryDate", (TextView) findViewById(R.id.txtSurgeryDate));
//                myTextView.put("surgeryMethod", (TextView) findViewById(R.id.txtSurgeryMethodName));
//                myTextView.put("cell", (TextView) findViewById(R.id.txtCellType));
//                myTextView.put("er", (TextView) findViewById(R.id.txtInputEr));
//                myTextView.put("pr", (TextView) findViewById(R.id.txtInputPr));
//                myTextView.put("her", (TextView) findViewById(R.id.txtHerType));
//                myTextView.put("fish", (TextView) findViewById(R.id.txtFishType));
//                myTextView.put("program", (TextView) findViewById(R.id.txtProgramName));
//                dataSnapshot = dataSnapshot.child("Users");
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                FirebaseUser users = auth.getCurrentUser();
//                String user = users.getUid();
//                dataSnapshot = dataSnapshot.child(user);
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String dataName = data.getKey();
//                    if (myTextView.containsKey(dataName)) {
//                        if (dataName.equals("surgeryMethod") || dataName.equals("program")) {
//                            StringBuilder str = new StringBuilder();
//                            for (DataSnapshot ds : data.child("record").getChildren()) {
//                                str.append(ds.getValue()).append("\n");
//                            }
//                            // 刪除最後一個\n
//                            str = str.deleteCharAt(str.length()-1);
//                            myTextView.get(dataName).setText(str.toString());
//                        } else {
//                            String dataValue = Objects.requireNonNull(data.getValue()).toString();
//                            myTextView.get(dataName).setText(dataValue);
//                        }
//
//                    }
//                }
//                progressDialog.dismiss();
//                isProgressDialogShow = false;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    /**
//     * scrollView 中嵌listView造成View高度異常，根據子View高度重置listVIew高度。
//     *
//     * @param listView
//     */
//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        if (listView == null) return;
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }
//}
