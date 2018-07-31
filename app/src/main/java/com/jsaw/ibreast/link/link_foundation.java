package com.jsaw.ibreast.link;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class link_foundation extends AppCompatActivity {

    class Center{
        String imageurl;
        String phone;
        String address;
        String url;
    }
    public static final ArrayList<Center> foundations=new ArrayList<>();
    private MyAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("link","onCreate");
        setContentView(R.layout.fragment_link_foundation);
        Log.d("link","setContent");
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        isProgressDialogShow = true;
        //connect time out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isProgressDialogShow){
                    progressDialog.dismiss();
                    Toast.makeText(link_foundation.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
        getData();
        Log.d("link","getData");
    }
    
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Center> mData;
        Context mcontext;
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            TextView mPhoneView;
            ImageView imageView;
            ViewHolder(View v) {
                super(v);
                mTextView = v.findViewById(R.id.info_text);
                imageView= v.findViewById(R.id.info_img);
                mPhoneView= v.findViewById(R.id.info_phonnum);
            }
        }

        MyAdapter(List<Center> data, Context context) {
            mData = data;
            mcontext=context;
        }

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.mTextView.setText(foundations.get(position).address);
            holder.mPhoneView.setText(foundations.get(position).phone);
            new DownloadImageTask(holder.imageView)
                    .execute(foundations.get(position).imageurl);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SpannableString s;
                    if(foundations.get(position).url!=null)
                        s = new SpannableString("電話：" + foundations.get(position).phone + "\n地址：" + foundations.get(position).address + "\n\n" + foundations.get(position).url);
                    else
                        s = new SpannableString("電話：" + foundations.get(position).phone + "\n地址：" + foundations.get(position).address + "\n\n");

                    Linkify.addLinks(s, Linkify.WEB_URLS);
                    final AlertDialog d = new AlertDialog.Builder(mcontext)
                            .setPositiveButton(android.R.string.ok, null)
                            .setMessage( s )
                            .create();
                    d.show();
                    // Make the textview clickable. Must be called after show()
                    ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                }
            });
        }
        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private void getData(){
        FirebaseDatabase.getInstance().getReference("link/foundation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                Log.d("link",ds.toString());
                for(int i = 0; i < ds.getChildrenCount();i++){
                    Center temp = new Center();
                    temp.imageurl = Objects.requireNonNull(ds.child(String.valueOf(i)).child("logo").getValue()).toString();
                    temp.phone = Objects.requireNonNull(ds.child(String.valueOf(i)).child("phone").getValue()).toString();
                    temp.address = Objects.requireNonNull(ds.child(String.valueOf(i)).child("address").getValue()).toString();
                    temp.url = Objects.requireNonNull(ds.child(String.valueOf(i)).child("url").getValue()).toString();
                    Log.d("link",temp.toString());
                    foundations.add(temp);
                }
                mAdapter = new MyAdapter(foundations,link_foundation.this);
                mRecyclerView = findViewById(R.id.list_view);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(link_foundation.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
                //mAdapter.notifyDataSetChanged();

                progressDialog.dismiss();
                isProgressDialogShow = false;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}