package com.jsaw.ibreast.note.body;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class note_body5 extends Fragment {
    private static final int[] IDS = new int[]{R.id.txtDate, R.id.txtOne, R.id.txtTwo, R.id.txtThree,
                                                R.id.txtFour, R.id.txtFive, R.id.txtSix, R.id.txtSeven, R.id.txtEight, R.id.txtNine, R.id.txtHurt};
    private static final String[] STRINGS = new String[]{"date", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        View view = inflater.inflate(R.layout.activity_note_body5, container, false);

        firebaseGetData(view);

        return view;
    }

    private void firebaseGetData(final View view) {
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LinkedHashMap<String, Object>> items = new LinkedList<>();
                ListView listView = view.findViewById(R.id.list_body5);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                dataSnapshot = dataSnapshot.child(user).child("症狀");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    LinkedHashMap<String, Object> item = new LinkedHashMap<>();
                    item.put("date", data.child("date").getValue().toString());
                    data = data.child("record");
                    int count = 0;
                    for (DataSnapshot record_data : data.getChildren()){
                        count++;
                        item.put(String.valueOf(count), record_data.getValue());
                    }

                    items.add(item);
                }

//                1. Context context 執行環境
//                2. List<? extends Map<String, ?>> data 帶入的資料
//                3. int resource Layout位置
//                4. String[] from data帶入資料的Key
//                5. int[] to Key的值要帶到哪個元件
                SimpleAdapter sa = new SimpleAdapter(getContext(), items, R.layout.body5_listview_item,
                        STRINGS, IDS);
                listView.setAdapter(sa);

                progressDialog.dismiss();
                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}