package com.syw.weiyu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paging.listview.PagingBaseAdapter;
import com.syw.weiyu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by songyouwei on 2015/4/27.
 */
public class NearByUserAdapter extends PagingBaseAdapter {

    public enum LOADTYPE {
        TYPE_REFRESH, TYPE_MORE;
    }

    public void setData(List<HashMap<String, String>> data) {
        nearbyusermapList.clear();
        nearbyusermapList.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<HashMap<String, String>> data) {
        nearbyusermapList.addAll(data);
        notifyDataSetChanged();
    }

    public List<HashMap<String, String>> getData() {
        return nearbyusermapList;
    }

    LayoutInflater mInflater;
    List<HashMap<String, String>> nearbyusermapList = new ArrayList<>();
//
    public NearByUserAdapter(Context ctx) {
        mInflater = LayoutInflater.from(ctx);
    }

    //在外面先定义，ViewHolder静态类
    static class ViewHolder {
        public ImageView portrait;
        public TextView name;
        public TextView address;
    }

//    @Override
    public int getCount() {
        return nearbyusermapList.size();
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
            convertView = mInflater.inflate(R.layout.wy_nearbyuser_lv_item, null);
            holder = new ViewHolder();
            holder.portrait = (ImageView) convertView.findViewById(R.id.nearbyuser_iv_portrait);
            holder.name = (TextView) convertView.findViewById(R.id.nearbyuser_tv_name);
            holder.address = (TextView) convertView.findViewById(R.id.nearbyuser_tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.portrait.setImageResource(
                nearbyusermapList.get(position).get("gender").equals("男")?
                        R.drawable.wy_icon_male:
                        R.drawable.wy_icon_female
        );
        holder.name.setText(nearbyusermapList.get(position).get("name"));
        holder.address.setText(nearbyusermapList.get(position).get("address"));

        return convertView;
    }
}
