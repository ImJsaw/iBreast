package com.jsaw.ibreast.note.treat;

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
import android.widget.TextClock;
import android.widget.TextView;
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
import java.util.List;

public class note_treat3 extends Fragment {
    private static final int[] IDS = new int[]{R.id.txtPartName, R.id.txtStartDate, R.id.txtEndDate};
    private static final String[] STRINGS = new String[]{"partName", "startDate", "endDate"};
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_note_treat2, container, false);
        firebaseGetData(view);

        return view;
    }

    private void firebaseGetData(final View view) {
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> items = new ArrayList<>();
                ListView listView = view.findViewById(R.id.list_treat2);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                dataSnapshot = dataSnapshot.child(user).child("標靶");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("startDate", data.child("startDate").getValue().toString());
                    item.put("endDate", data.child("endDate").getValue().toString());
                    StringBuilder str = new StringBuilder();
                    for (DataSnapshot ds : data.child("part").getChildren()) {
                        str.append(ds.getValue()).append(", ");
                    }
                    // 刪除最後一個,
                    str = str.deleteCharAt(str.length()-2);
                    item.put("partName", str);
                    items.add(item);
                }

//                1. Context context 執行環境
//                2. List<? extends Map<String, ?>> data 帶入的資料
//                3. int resource Layout位置
//                4. String[] from data帶入資料的Key
//                5. int[] to Key的值要帶到哪個元件
                SimpleAdapter sa = new SimpleAdapter(getContext(), items, R.layout.treat3_listview_item,
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
