package com.syw.weiyu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Comment;
import io.rong.imkit.RongIM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by songyouwei on 2015/4/27.
 */
public class CommentsAdapter extends BaseAdapter {
    List<Comment> comments = new ArrayList<>();
    public void set(List<Comment> list) {
        this.comments.clear();
        this.comments.addAll(list);
        notifyDataSetChanged();
    }
    public void append(List<Comment> list) {
        this.comments.addAll(list);
        notifyDataSetChanged();
    }
    public void append(Comment comment) {
        this.comments.add(comment);
        notifyDataSetChanged();
    }

    LayoutInflater mInflater;
    Context ctx;
    public CommentsAdapter(Context ctx) {
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    //在外面先定义，ViewHolder静态类
    static class ViewHolder {
        public ImageView portrait;
        public TextView name;
        public TextView floor;
        public TextView time;
        public TextView content;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
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
            convertView = mInflater.inflate(R.layout.wy_comment_lv_item, null);
            holder = new ViewHolder();
            holder.portrait = (ImageView) convertView.findViewById(R.id.comment_iv_gender);
            holder.name = (TextView) convertView.findViewById(R.id.comment_tv_name);
            holder.floor = (TextView) convertView.findViewById(R.id.comment_tv_floor);
            holder.time = (TextView) convertView.findViewById(R.id.comment_tv_time);
            holder.content = (TextView) convertView.findViewById(R.id.comment_tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Comment comment = comments.get(position);
        holder.portrait.setImageResource(comment.getUserGender().equals("男")?R.drawable.wy_icon_male:R.drawable.wy_icon_female);
        holder.name.setText(comment.getUserName());
        holder.floor.setText(1+position+"楼");
        holder.time.setText(new SimpleDateFormat("MM-dd kk:mm").format(new Date(comment.getTimestamp())));
        holder.content.setText(comment.getContent());

        //点击评论时开启私聊
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startPrivateChat(ctx, comment.getUserId(), comment.getUserName()+"（私聊）");
            }
        });

        return convertView;
    }
}
