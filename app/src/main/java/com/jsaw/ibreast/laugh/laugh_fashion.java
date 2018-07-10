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
import com.jsaw.ibreast.open_pdf;

import java.util.Objects;

public class laugh_fashion extends Fragment {
    ImageButton btn_bra;
    ImageButton btn_hair;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        return inflater.inflate(R.layout.fragment_laugh_fashion, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_bra= Objects.requireNonNull(getView()).findViewById(R.id.Btn_bra);
        btn_bra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Objects.requireNonNull(getActivity()), open_pdf.class).putExtra("URL",
                        "https://drive.google.com/file/d/14ZXhIv_sk4lTgexErDb8NcrA8jEYoj8A/view"));
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
    }
}