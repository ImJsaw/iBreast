package com.jsaw.ibreast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;

public class askAdapter extends BaseAdapter {
    private LinkedList<askData> mData;
    private Context mContext;
    private int currentItem = -1; //clicked item log

    askAdapter(LinkedList<askData> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    private static class ViewHolder {
        private LinearLayout showArea;
        private TextView ask_list_title;
        private TextView ask_list_text;
        private RelativeLayout hideArea;
    }

    static class askData {
        String title;
        String text;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.ask_view, parent, false);
            holder = new ViewHolder();
            holder.showArea =  view.findViewById(R.id.layout_showArea);
            holder.ask_list_title =  view.findViewById(R.id.ask_list_title);
            holder.ask_list_text =  view.findViewById(R.id.ask_list_text);
            holder.hideArea = view.findViewById(R.id.layout_hideArea);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        askData item = mData.get(position);
        // set tag
        holder.showArea.setTag(position);
        holder.ask_list_title.setText(item.title);
        holder.ask_list_text.setText(item.text);
        //set visible of clicked
        if (currentItem == position) {
            holder.hideArea.setVisibility(View.VISIBLE);
        } else {
            holder.hideArea.setVisibility(View.GONE);
        }
        holder.showArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //click again
                    currentItem = -1; //give a usable position ==> all hide
                } else {
                    currentItem = tag;
                }
                //renew
                notifyDataSetChanged();
            }
        });
        return  view;
    }


}

