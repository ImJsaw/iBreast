package com.jsaw.ibreast;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class aboutBox {
    private static String versionMame(Context context){
        try{
            return context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "UnKnown";
        }
    }

    static void show(Activity callingActivity){
        TextView helpText,titleText;
        Button check;
        SpannableString text = new SpannableString(callingActivity.getString(R.string.aboutContent)+"\n\n"
                +"Version"+versionMame(callingActivity)+"\n\n");
        final Dialog dialog = new Dialog(callingActivity);
        dialog.setContentView(R.layout.about_box);

        titleText = dialog.findViewById(R.id.titleView);
        titleText.setText(R.string.about);
        titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        helpText = dialog.findViewById(R.id.aboutText);
        helpText.setTextColor(Color.BLACK);
        helpText.setText(text);

        check = dialog.findViewById(R.id.button);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
