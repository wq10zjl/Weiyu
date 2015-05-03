package com.syw.weiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paging.listview.PagingBaseAdapter;
import com.syw.weiyu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by songyouwei on 2015/4/27.
 */
public class ShuoshuoAdapter extends PagingBaseAdapter<HashMap<String, String>> {

    public enum LOADTYPE {
        TYPE_REFRESH, TYPE_MORE;
    }

    public void setData(List<HashMap<String, String>> data) {
        shuoshuomapList.clear();
        shuoshuomapList.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<HashMap<String, String>> data) {
        shuoshuomapList.addAll(data);
        notifyDataSetChanged();
    }

    public List<HashMap<String, String>> getData() {
        return shuoshuomapList;
    }
    LayoutInflater mInflater;
    List<HashMap<String, String>> shuoshuomapList = new ArrayList<>();

    public ShuoshuoAdapter(Context ctx) {
        mInflater = LayoutInflater.from(ctx);
    }

    //在外面先定义，ViewHolder静态类
    static class ViewHolder {
        public TextView name;
        public TextView address;
        public TextView time;
        public TextView content;
    }

    @Override
    public int getCount() {
        return shuoshuomapList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //然后重写getView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.wy_shuoshuo_lv_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.shuoshuo_tv_name);
            holder.address = (TextView) convertView.findViewById(R.id.shuoshuo_tv_address);
            holder.time = (TextView) convertView.findViewById(R.id.shuoshuo_tv_time);
            holder.content = (TextView) convertView.findViewById(R.id.shuoshuo_tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(shuoshuomapList.get(position).get("shuoshuo_tv_name"));
        holder.address.setText(shuoshuomapList.get(position).get("shuoshuo_tv_address"));
        holder.time.setText(shuoshuomapList.get(position).get("shuoshuo_tv_time"));
        holder.content.setText(shuoshuomapList.get(position).get("shuoshuo_tv_content"));
        //random bg
//            int[] bgs = new int[]{R.drawable.b1,R.drawable.b2,R.drawable.b3,R.drawable.b4,R.drawable.b5};
//            holder.content.setBackgroundColor(R.color.);

        return convertView;
    }
}
