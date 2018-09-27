package com.jsaw.ibreast.laugh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;
import com.jsaw.ibreast.cure;
import com.jsaw.ibreast.listAdapter;
import com.jsaw.ibreast.open_pdf;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class laugh_music extends Fragment {

    private String url[] = new String[50];
    private List<listAdapter.listData> mData = null;
    private listAdapter mAdapter = null;
    private ListView listView;
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        return inflater.inflate(R.layout.activity_cure, container, false);
    }



    private void getData() {
        mData = new LinkedList<>();
        FirebaseDatabase.getInstance().getReference("laugh").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    listAdapter.listData c = new listAdapter.listData();
                    c.title = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.url = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("url").getValue()).toString();
                    url[i] = c.url;
                    mData.add(c);
                }
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //listView
        mAdapter = new listAdapter((LinkedList<listAdapter.listData>) mData, getActivity());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("URL:", url[position]);
                startActivity(new Intent().setClass(getActivity(), open_pdf.class).putExtra("URL", url[position]));
            }
        });
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = Objects.requireNonNull(getView()).findViewById(R.id.list_cure);
        //waiting dialog
        progressDialog = new ProgressDialog(getActivity());
        if(getActivity() == null) Log.d("MUSIC","cannot get context");
        else Log.d("MUSIC","get context");
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        isProgressDialogShow = true;
        //connect time out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isProgressDialogShow) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
        getData();

        /*
        btn_bra= Objects.requireNonNull(getView()).findViewById(R.id.Btn_bra);
        btn_bra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Objects.requireNonNull(getActivity()), open_pdf.class).putExtra("URL",
                        "https://drive.google.com/open?id=1oAfAyppd8Wl1CQJTRbOw9syejYLINMK8"));
            }
        });

        btn_hair= getView().findViewById(R.id.Btn_hair);
        btn_hair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Objects.requireNonNull(getActivity()), open_pdf.class).putExtra("URL",
                        "https://drive.google.com/open?id=16XO2N6D9FYfUSzRyBRF7hJUhwM5JXB3e"));
            }
        });
        */
    }
}