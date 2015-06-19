package com.syw.weiyu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.syw.weiyu.util.TimeUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import java.util.*;

/**
 * Created by songyouwei on 2015/4/27.
 */
public class ShuoshuosAdapter extends PagingBaseAdapter {

    List<Shuoshuo> shuoshuos = new ArrayList<>();
    Picasso picasso;
    LayoutInflater mInflater;
    Context ctx;

    public ShuoshuosAdapter(Context ctx) {
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
        picasso = Picasso.with(ctx);
    }

    public void set(List<Shuoshuo> list) {
        this.shuoshuos.clear();
        this.shuoshuos.addAll(list);
        notifyDataSetChanged();
    }

    public void append(List<Shuoshuo> list) {
        this.shuoshuos.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        //pre load
        for (Shuoshuo shuoshuo: shuoshuos) {
            if (shuoshuo.getImgUrl() != null) picasso.load(shuoshuo.getImgUrl()).fetch();
        }

        super.notifyDataSetChanged();
    }

    //在外面先定义，ViewHolder静态类
    private class ViewHolder {
        public ViewHolder(View convertView) {
            this.convertView = convertView;
            this.name = (TextView) convertView.findViewById(R.id.shuoshuo_tv_name);
            this.address = (TextView) convertView.findViewById(R.id.shuoshuo_tv_address);
            this.time = (TextView) convertView.findViewById(R.id.shuoshuo_tv_time);
            this.content = (TextView) convertView.findViewById(R.id.shuoshuo_tv_content);
            this.commentCount = (TextView) convertView.findViewById(R.id.shuoshuo_tv_comment_count);
            this.liked = (ImageView) convertView.findViewById(R.id.ic_like);
            this.likedCount = (TextView) convertView.findViewById(R.id.shuoshuo_tv_liked_count);
            this.img = (ImageView) convertView.findViewById(R.id.shuoshuo_iv_img);
        }
        public View convertView;
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
        final Shuoshuo shuoshuo = shuoshuos.get(position);
        final ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.wy_shuoshuo_lv_item_v2, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(shuoshuo.getUserName());
        holder.address.setText(shuoshuo.getLocation() == null ? shuoshuo.getAddressStr() : shuoshuo.getLocation().getProvince() + shuoshuo.getLocation().getCity() + shuoshuo.getLocation().getDistrict());
        holder.time.setText(TimeUtil.timeDiff(System.currentTimeMillis(), shuoshuo.getTimestamp()));
        holder.content.setText(shuoshuo.getContent());
        holder.commentCount.setText(String.valueOf(shuoshuo.getCommentCount()) + "评论");
        holder.likedCount.setText(String.valueOf(shuoshuo.getLikedCount()));

        String imgUrl = shuoshuo.getImgUrl();
        if (imgUrl!=null) {
            imgUrl = BmobImageDao.getSingedUrl(imgUrl);
            picasso.load(imgUrl).placeholder(R.drawable.bg_preload).error(R.drawable.bg_preload).into(holder.img);
        } else {
            Drawable randomBg = RandomBg.getBgColorDrawable(shuoshuo.getTimestamp());
            picasso.load(imgUrl).placeholder(randomBg).error(randomBg).into(holder.img);
        }

        ItemOnClickListener itemOnClickListener = new ItemOnClickListener(holder,shuoshuo);
        holder.convertView.setOnClickListener(itemOnClickListener);
        holder.commentCount.setOnClickListener(itemOnClickListener);
        holder.liked.setOnClickListener(itemOnClickListener);

        return convertView;
    }

    private class ItemOnClickListener implements View.OnClickListener {
        private ViewHolder holder;
        private Shuoshuo shuoshuo;

        public ItemOnClickListener(ViewHolder holder, Shuoshuo shuoshuo) {
            this.holder = holder;
            this.shuoshuo = shuoshuo;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.shuoshuo_container:
                case R.id.shuoshuo_tv_comment_count:
                    if (ctx instanceof ShuoshuoDetailActivity) {
                        //如果是详情页
                        switchImgView(holder);
                    } else {
                        //进说说详情页
                        Intent intent = new Intent(ctx, ShuoshuoDetailActivity.class);
                        intent.putExtra("shuoshuo", shuoshuo);
                        ctx.startActivity(intent);
                    }
                    break;
                case R.id.ic_like:
                    WeiyuApi.get().addLiked(shuoshuo);
                    ((ImageView)v).setImageResource(R.drawable.wy_ic_card_liked);
                    v.setClickable(false);
                    shuoshuo.setLikedCount(shuoshuo.getLikedCount() + 1);
                    holder.likedCount.setText(String.valueOf(shuoshuo.getLikedCount()));
                    break;
            }
        }

    }

    boolean onlyhasImg = false;
    //切换只显示背景图片
    private void switchImgView(ViewHolder holder) {
        if (onlyhasImg) {
            holder.name.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.VISIBLE);
            holder.address.setVisibility(View.VISIBLE);
            holder.commentCount.setVisibility(View.VISIBLE);
            holder.liked.setVisibility(View.VISIBLE);
            holder.likedCount.setVisibility(View.VISIBLE);
            onlyhasImg = false;
        } else {
            holder.name.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);
            holder.content.setVisibility(View.INVISIBLE);
            holder.address.setVisibility(View.INVISIBLE);
            holder.commentCount.setVisibility(View.INVISIBLE);
            holder.liked.setVisibility(View.INVISIBLE);
            holder.likedCount.setVisibility(View.INVISIBLE);
            onlyhasImg = true;
        }
    }
}
