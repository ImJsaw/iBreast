package com.jsaw.ibreast.talk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jsaw.ibreast.R;
import com.jsaw.ibreast.open_movie;

import java.util.Objects;

public class talk_expert extends Fragment {
    ImageButton btn_2movie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_talk, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_2movie= getView().findViewById(R.id.btn_sleep);
        btn_2movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Objects.requireNonNull(getActivity()), open_movie.class).putExtra("URL",
                        "https://www.youtube.com/watch?v=uKTn88nLWD4&list=PLcYwYvFNfjqZ1K_StNZHnvvcpA-_XvFiX&index=1"));
            }
        });
    }

}