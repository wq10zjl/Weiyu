package com.syw.weiyu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.paging.listview.PagingBaseAdapter;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Shuoshuo;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by songyouwei on 2015/4/27.
 */
public class ShuoshuoListAdapter extends PagingBaseAdapter {
    List<Shuoshuo> shuoshuos = new ArrayList<>();
    public void set(List<Shuoshuo> list) {
        this.shuoshuos.clear();
        this.shuoshuos.addAll(list);
        notifyDataSetChanged();
    }
    public void append(List<Shuoshuo> list) {
        this.shuoshuos.addAll(list);
        notifyDataSetChanged();
    }

    LayoutInflater mInflater;
    public ShuoshuoListAdapter(Context ctx) {
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
        return shuoshuos.size();
    }

    @Override
    public Object getItem(int position) {
        return shuoshuos.get(position);
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
        Shuoshuo shuoshuo = shuoshuos.get(position);
        holder.name.setText(shuoshuo.getUserName());
        holder.address.setText(shuoshuo.getLocation().getProvince()+shuoshuo.getLocation().getCity()+shuoshuo.getLocation().getDistrict());
        holder.time.setText(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(new Date(shuoshuo.getTimestamp())));
        holder.content.setText(shuoshuo.getContent());

        //随机背景图
        int[] bgs = new int[]{
                R.drawable.s_1,
                R.drawable.s_2,
                R.drawable.s_3,
                R.drawable.s_4,
                R.drawable.s_5,
                R.drawable.s_6,
                R.drawable.s_7,
                R.drawable.s_8,
                R.drawable.s_9,
                R.drawable.s_10,
                R.drawable.s_11,
                R.drawable.s_12,
                R.drawable.s_13,
                R.drawable.s_14,
                R.drawable.s_15,
                R.drawable.s_16,
                R.drawable.s_17,
                R.drawable.s_18,
                R.drawable.s_19,
                R.drawable.s_20,
                R.drawable.s_21,
                R.drawable.s_22,
                R.drawable.s_23,
                R.drawable.s_24,
                R.drawable.s_25,
                R.drawable.s_26,
                R.drawable.s_27,
                R.drawable.s_28,
                R.drawable.s_0,
        };
        int i = Math.abs(shuoshuo.hashCode()%29);
        convertView.setBackgroundResource(bgs[i]);

        return convertView;
    }
}
