package com.jsaw.ibreast.laugh;

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
import com.jsaw.ibreast.open_pdf;
import java.util.Objects;


public class laugh_night extends Fragment {
    ImageButton btn_sleep;
    ImageButton btn_relax;
    ImageButton btn_message;
    ImageButton btn_mind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laugh_night, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        btn_sleep= Objects.requireNonNull(getView()).findViewById(R.id.btn_sleep);
        btn_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Objects.requireNonNull(getActivity()), open_pdf.class).putExtra("URL",
                        "http://drive.google.com/viewerng/viewer?embedded=true&url=http://13.231.194.159/laugh_sleep.pdf"));
            }
        });

        btn_relax= getView().findViewById(R.id.btn_relax);
        btn_relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Objects.requireNonNull(getActivity()), open_movie.class).putExtra("URL",
                        "https://www.youtube.com/watch?v=CkocbsNxfVs&list=PLcYwYvFNfjqb0B9P2DlBtG4_yUt3LTgSd&index=1"));
            }
        });

        btn_message= getView().findViewById(R.id.btn_message);
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Objects.requireNonNull(getActivity()), open_pdf.class).putExtra("URL",
                        "http://drive.google.com/viewerng/viewer?embedded=true&url=http://13.231.194.159/laugh_message.pdf"));

            }
        });

        btn_mind= getView().findViewById(R.id.btn_mind);
        btn_mind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Objects.requireNonNull(getActivity()), open_pdf.class).putExtra("URL",
                        "http://drive.google.com/viewerng/viewer?embedded=true&url=http://13.231.194.159/laugh_less.pdf"));
            }
        });
    }



}