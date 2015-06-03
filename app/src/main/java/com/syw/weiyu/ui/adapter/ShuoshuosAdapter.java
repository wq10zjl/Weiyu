package com.syw.weiyu.ui.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.paging.listview.PagingBaseAdapter;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.ui.shuoshuo.ShuoshuoDetailActivity;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by songyouwei on 2015/4/27.
 */
public class ShuoshuosAdapter extends PagingBaseAdapter {
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
    public void addOnTop(Shuoshuo shuoshuo) {
        this.shuoshuos.add(0,shuoshuo);
        notifyDataSetChanged();
    }

    LayoutInflater mInflater;
    Context ctx;
    public ShuoshuosAdapter(Context ctx) {
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    //在外面先定义，ViewHolder静态类
    static class ViewHolder {
        public TextView name;
        public TextView address;
        public TextView time;
        public TextView content;
        public TextView commentCount;
        public ImageView liked;
        public TextView likedCount;
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
            holder.commentCount = (TextView) convertView.findViewById(R.id.shuoshuo_tv_comment_count);
            holder.liked = (ImageView) convertView.findViewById(R.id.ic_like);
            holder.likedCount = (TextView) convertView.findViewById(R.id.shuoshuo_tv_liked_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Shuoshuo shuoshuo = shuoshuos.get(position);
        holder.name.setText(shuoshuo.getUserName());
        holder.address.setText(shuoshuo.getLocation()==null?shuoshuo.getAddressStr():shuoshuo.getLocation().getProvince()+shuoshuo.getLocation().getCity()+shuoshuo.getLocation().getDistrict());
        holder.time.setText(new SimpleDateFormat("MM-dd kk:mm").format(new Date(shuoshuo.getTimestamp())));
        holder.content.setText(shuoshuo.getContent());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,ShuoshuoDetailActivity.class);
                intent.putExtra("shuoshuo",shuoshuo);
                ctx.startActivity(intent);
            }
        });
        holder.commentCount.setText(shuoshuo.getCommentCount());
        holder.likedCount.setText(shuoshuo.getLikedCount());


        /*设置背景色*/
        //1/2白色
        if (shuoshuo.getTimestamp()%2 == 0) {
            convertView.setBackgroundResource(R.drawable.s_1);
            holder.name.setTextColor(Color.BLACK);
            holder.address.setTextColor(Color.BLACK);
            holder.time.setTextColor(Color.BLACK);
            holder.content.setTextColor(Color.BLACK);
            holder.liked.setImageResource(R.drawable.wy_ic_card_like_grey);
        } else {
            //随机背景图[非白色]
            int[] bgs = new int[]{
                    R.drawable.s_2,
                    R.drawable.s_3,
                    R.drawable.s_4,
                    R.drawable.s_5,
                    R.drawable.s_6,
                    R.drawable.s_7,
                    R.drawable.s_8,
                    R.drawable.s_9,
                    R.drawable.s_10,
            };
            int i = Math.abs((int)shuoshuo.getTimestamp()%9);//0~8
            convertView.setBackgroundResource(bgs[i]);
            holder.name.setTextColor(Color.WHITE);
            holder.address.setTextColor(Color.WHITE);
            holder.time.setTextColor(Color.WHITE);
            holder.content.setTextColor(Color.WHITE);
            holder.liked.setImageResource(R.drawable.wy_ic_card_like);
        }

        return convertView;
    }
}
