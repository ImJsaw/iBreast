package com.jsaw.ibreast.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jsaw.ibreast.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckboxListviewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<HashMap<String, Object>> itemList = new ArrayList<>();
    //用于标记被选中的checkbox
    private Map<Integer, Boolean> isCheckedMap = new HashMap<>();

    public CheckboxListviewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //给ListView设置数据源用的方法
    public void setItems(List<HashMap<String, Object>> itemList) {
        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {

            holder = new ViewHolder();

            view = inflater.inflate(R.layout.item, viewGroup, false);
            holder.tvItem = view.findViewById(R.id.chitxt_listview);
            holder.checkBox = view.findViewById(R.id.checkbox_listview);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvItem.setText(itemList.get(i).toString());

        //判断是否记录了勾选了的Checkbox
        if (isCheckedMap.get(i) != null && isCheckedMap.get(i)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = v.findViewById(R.id.checkbox_listview);
                if (checkBox.isChecked()) {
                    isCheckedMap.put(i, true);

                } else {
                    isCheckedMap.remove(i);
                }

            }
        });

        return view;
    }

    class ViewHolder {
        TextView tvItem;
        CheckBox checkBox;
    }
}
