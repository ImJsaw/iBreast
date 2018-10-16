package com.jsaw.ibreast.note.body;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jsaw.ibreast.R;

public class note_body5 extends Fragment {
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("處理中,請稍候...");
//        progressDialog.show();
//        isProgressDialogShow = true;
//        //connect time out
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (isProgressDialogShow) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getContext(), "連線逾時", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, 5000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_body5, container, false);

        return view;
    }
}