package com.jsaw.ibreast.link;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.jsaw.ibreast.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class link_center extends AppCompatActivity {
    class Center{
        String name;
        String phone;
        String address;
    }
    private String TAG = "link_center";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    public static final ArrayList<Center> north=new ArrayList<>();
    public static final ArrayList<Center> center=new ArrayList<>();
    public static final ArrayList<Center> south=new ArrayList<>();
    public static final ArrayList<Center> east=new ArrayList<>();
    private String data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_link_center);
        new getData(this).execute();

    }

    private void JSONanalyse(){
        try{
            north.clear();
            center.clear();
            south.clear();
            east.clear();
            JSONArray jsonArray = new JSONArray(data);
            for(int k=0;k<jsonArray.length();k++) {
                if (k == 0)
                    Log.i(TAG, "北區+++++++");
                else if (k == 1)
                    Log.i(TAG, "中區+++++++");
                else if (k == 2)
                    Log.i(TAG, "南區+++++++");
                else if (k == 3)
                    Log.i(TAG, "東區+++++++");
                else
                    Log.i(TAG, "無法識別+++++++");
                JSONObject jsonObject = new JSONObject(jsonArray.getString(k));
                //Log.i(TAG, "length: "+jsonObject.toString());
                if (k == 0) {
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject data2 = new JSONObject(jsonObject.getString(String.valueOf(i)));
                        Center temp = new Center();
                        String number = data2.getString("number");
                        temp.name = data2.getString("unit");
                        temp.phone = data2.getString("phone");
                        temp.address = data2.getString("address");
                        north.add(temp);
                        Log.i(TAG, "123------ >  " + number + "  " + temp.name + "  " + temp.phone + "  " + temp.address);
                    }
                }
                else if(k==1){
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject data2 = new JSONObject(jsonObject.getString(String.valueOf(i)));
                        Center temp = new Center();
                        String number = data2.getString("number");
                        temp.name = data2.getString("unit");
                        temp.phone = data2.getString("phone");
                        temp.address = data2.getString("address");
                        center.add(temp);
                        Log.i(TAG, "123------ >  " + number + "  " + temp.name + "  " + temp.phone + "  " + temp.address);
                    }
                }
                else if(k==2){
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject data2 = new JSONObject(jsonObject.getString(String.valueOf(i)));
                        Center temp = new Center();
                        String number = data2.getString("number");
                        temp.name = data2.getString("unit");
                        temp.phone = data2.getString("phone");
                        temp.address = data2.getString("address");
                        south.add(temp);
                        Log.i(TAG, "123------ >  " + number + "  " + temp.name + "  " + temp.phone + "  " + temp.address);
                    }
                }
                else if(k==3){
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject data2 = new JSONObject(jsonObject.getString(String.valueOf(i)));
                        Center temp = new Center();
                        String number = data2.getString("number");
                        temp.name = data2.getString("unit");
                        temp.phone = data2.getString("phone");
                        temp.address = data2.getString("address");
                        east.add(temp);
                        Log.i(TAG, "123------ >  " + number + "  " + temp.name + "  " + temp.phone + "  " + temp.address);
                    }
                }
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void addTableRow(TableLayout tl, String name, String phone, String address){

        LayoutInflater inflater = this.getLayoutInflater();
        TableRow tr = (TableRow)inflater.inflate(R.layout.table_row, tl, false);

        // Add First Column
        TextView Name = (TextView)tr.findViewById(R.id.Name);
        Name.setText(name);

        // Add the 3rd Column
        TextView Phone = (TextView)tr.findViewById(R.id.Phone);
        Phone.setText(phone);

        TextView Address = (TextView)tr.findViewById(R.id.Address);
        Address.setText(address);

        tl.addView(tr);
    }

    private class getData extends AsyncTask<String, String, String>
    {
        //ProgressDialog pdLoading = new ProgressDialog(getApplicationContext());
        HttpURLConnection conn;
        URL url = null;
        ProgressDialog pDialog;
        private Context mcontext;

        getData(Context context){
            this.mcontext=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pDialog = new ProgressDialog(mcontext);
                    pDialog.setMessage("Loading..");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }
            });
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://13.231.194.159/link_center.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
//                Uri.Builder builder = new Uri.Builder()
//                        .appendQueryParameter("username", params[0])
//                        .appendQueryParameter("password", params[1]);
//                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            data = result;
            JSONanalyse();

            TableLayout ll =  findViewById(R.id.NorthTable);
            TableLayout cl =  findViewById(R.id.CenterTable);
            TableLayout sl =  findViewById(R.id.SouthTable);
            TableLayout el =  findViewById(R.id.EastTable);
            for (int i = 0; i <north.size(); i++) {
                addTableRow(ll,north.get(i).name,north.get(i).phone,north.get(i).address);
            }
            for (int i = 0; i <center.size(); i++) {
                addTableRow(cl,center.get(i).name,center.get(i).phone,center.get(i).address);
            }
            for (int i = 0; i < south.size(); i++) {
                addTableRow(sl,south.get(i).name,south.get(i).phone,south.get(i).address);
            }
            for (int i = 0; i <east.size(); i++) {
                addTableRow(el,east.get(i).name,east.get(i).phone,east.get(i).address);
            }

        }

    }



}

