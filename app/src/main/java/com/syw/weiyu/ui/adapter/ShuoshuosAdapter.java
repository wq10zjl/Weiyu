package com.syw.weiyu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.paging.listview.PagingBaseAdapter;
import com.squareup.picasso.Picasso;
import com.syw.weiyu.R;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.dao.img.BmobImageDao;
import com.syw.weiyu.ui.shuoshuo.ShuoshuoDetailActivity;
import com.syw.weiyu.util.RandomBg;
import com.syw.weiyu.util.StringUtil;
import com.syw.weiyu.util.TimeUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

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
        public ImageView img;
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
        final ViewHolder holder;
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
            holder.img = (ImageView) convertView.findViewById(R.id.shuoshuo_iv_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Shuoshuo shuoshuo = shuoshuos.get(position);
        holder.name.setText(shuoshuo.getUserName());
        holder.address.setText(shuoshuo.getLocation()==null?shuoshuo.getAddressStr():shuoshuo.getLocation().getProvince()+shuoshuo.getLocation().getCity()+shuoshuo.getLocation().getDistrict());
//        holder.time.setText(new SimpleDateFormat("MM-dd kk:mm").format(new Date(shuoshuo.getTimestamp())));
        holder.time.setText(TimeUtil.timeDiff(System.currentTimeMillis(), shuoshuo.getTimestamp()));
        holder.content.setText(shuoshuo.getContent());
        holder.commentCount.setText(String.valueOf(shuoshuo.getCommentCount()) + "评论");
        holder.likedCount.setText(String.valueOf(shuoshuo.getLikedCount()));
        String imgUrl = shuoshuo.getImgUrl();
        if (!StringUtil.isEmpty(imgUrl)) {
            imgUrl = BmobImageDao.getSingedUrl(imgUrl);
            Picasso.with(ctx).load(imgUrl).into(holder.img);
        }

        //点内容时跳转
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctx instanceof ShuoshuoDetailActivity) {
                    //如果是详情页，就开启私聊
                    RongIM.getInstance().startConversation(ctx, Conversation.ConversationType.PRIVATE, shuoshuo.getUserId(), shuoshuo.getUserName());
                } else {
                    //进说说详情页
                    Intent intent = new Intent(ctx, ShuoshuoDetailActivity.class);
                    intent.putExtra("shuoshuo", shuoshuo);
                    ctx.startActivity(intent);
                }
            }
        });
        //点评论时跳转
        holder.commentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctx instanceof ShuoshuoDetailActivity) {
                    //如果是详情页，就开启私聊
                    RongIM.getInstance().startConversation(ctx, Conversation.ConversationType.PRIVATE, shuoshuo.getUserId(), shuoshuo.getUserName());
                } else {
                    //进说说详情页
                    Intent intent = new Intent(ctx, ShuoshuoDetailActivity.class);
                    intent.putExtra("shuoshuo", shuoshuo);
                    ctx.startActivity(intent);
                }
            }
        });
        //点❤
        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeiyuApi.get().addLiked(shuoshuo);
                holder.liked.setImageResource(R.drawable.wy_ic_card_liked);
                holder.liked.setClickable(false);
                shuoshuo.setLikedCount(shuoshuo.getLikedCount()+1);
                holder.likedCount.setText(String.valueOf(shuoshuo.getLikedCount()));
            }
        });

        /*设置背景色*/
        //1/2白色
        long n = shuoshuo.getTimestamp();
        if (n%2 == 0) {
            convertView.setBackgroundResource(R.drawable.s_1);
            holder.name.setTextColor(Color.BLACK);
            holder.address.setTextColor(Color.BLACK);
            holder.time.setTextColor(Color.BLACK);
            holder.content.setTextColor(Color.BLACK);
            holder.commentCount.setTextColor(Color.BLACK);
            holder.likedCount.setTextColor(Color.BLACK);
            holder.liked.setImageResource(R.drawable.wy_ic_card_like_grey);
        } else {
            //随机背景图[非白色]
            convertView.setBackgroundResource(RandomBg.getBgResId(n));
            holder.name.setTextColor(Color.WHITE);
            holder.address.setTextColor(Color.WHITE);
            holder.time.setTextColor(Color.WHITE);
            holder.content.setTextColor(Color.WHITE);
            holder.commentCount.setTextColor(Color.WHITE);
            holder.likedCount.setTextColor(Color.WHITE);
            holder.liked.setImageResource(R.drawable.wy_ic_card_like);
        }

        return convertView;
    }
}
