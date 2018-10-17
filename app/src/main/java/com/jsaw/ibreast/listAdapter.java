package com.jsaw.ibreast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.LinkedList;

public class listAdapter extends BaseAdapter {
    private LinkedList<listData> mData;
    private Context mContext;

    public listAdapter(LinkedList<listData> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    static public class listData {
        public String title;
        public String url;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        v = LayoutInflater.from( mContext ).inflate( R.layout.list_view, parent, false );
        TextView t = v.findViewById(R.id.text);
        t.setText(mData.get(position).title);
        return v;
    }
}
