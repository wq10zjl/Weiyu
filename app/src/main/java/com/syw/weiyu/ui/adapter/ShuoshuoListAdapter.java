package com.syw.weiyu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.paging.listview.PagingBaseAdapter;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by songyouwei on 2015/4/27.
 */
public class ShuoshuoListAdapter extends PagingBaseAdapter {
    ShuoshuoList shuoshuoList;
    public void set(ShuoshuoList list) {
        this.shuoshuoList = list;
        notifyDataSetChanged();
    }
    public void append(ShuoshuoList list) {
        this.shuoshuoList.append(list.get());
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
        return shuoshuoList.get().size();
    }

    @Override
    public Object getItem(int position) {
        return shuoshuoList.get().get(position);
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
        Shuoshuo shuoshuo = shuoshuoList.get().get(position);
        holder.name.setText(shuoshuo.getUserName());
        holder.address.setText(shuoshuo.getLocation().getProvince()+shuoshuo.getLocation().getCity()+shuoshuo.getLocation().getDistrict());
        holder.time.setText(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH).format(new Timestamp(shuoshuo.getTimestamp() * 1000)));
        holder.content.setText(shuoshuo.getContent());

        return convertView;
    }
}
