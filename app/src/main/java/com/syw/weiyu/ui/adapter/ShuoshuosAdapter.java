package com.syw.weiyu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.paging.listview.PagingBaseAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.syw.weiyu.R;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.dao.img.BmobImageDao;
import com.syw.weiyu.ui.shuoshuo.ImgViewerActivity;
import com.syw.weiyu.ui.shuoshuo.ShuoshuoDetailActivity;
import com.syw.weiyu.ui.shuoshuo.ShuoshuoItemView;
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
    static class ViewHolder implements Target {
        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }
        public View convertView;
        public TextView name;
        public TextView address;
        public TextView time;
        public TextView content;
        public TextView commentCount;
        public ImageView liked;
        public TextView likedCount;
//        public ImageView img;

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            convertView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            convertView.setBackgroundDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
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
            convertView = mInflater.inflate(R.layout.wy_shuoshuo_lv_item_v2, null);
            holder = new ViewHolder(convertView);
            holder.name = (TextView) convertView.findViewById(R.id.shuoshuo_tv_name);
            holder.address = (TextView) convertView.findViewById(R.id.shuoshuo_tv_address);
            holder.time = (TextView) convertView.findViewById(R.id.shuoshuo_tv_time);
            holder.content = (TextView) convertView.findViewById(R.id.shuoshuo_tv_content);
            holder.commentCount = (TextView) convertView.findViewById(R.id.shuoshuo_tv_comment_count);
            holder.liked = (ImageView) convertView.findViewById(R.id.ic_like);
            holder.likedCount = (TextView) convertView.findViewById(R.id.shuoshuo_tv_liked_count);
//            holder.img = (ImageView) convertView.findViewById(R.id.shuoshuo_iv_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Shuoshuo shuoshuo = shuoshuos.get(position);
        holder.name.setText(shuoshuo.getUserName());
        holder.address.setText(shuoshuo.getLocation() == null ? shuoshuo.getAddressStr() : shuoshuo.getLocation().getProvince() + shuoshuo.getLocation().getCity() + shuoshuo.getLocation().getDistrict());
//        holder.time.setText(new SimpleDateFormat("MM-dd kk:mm").format(new Date(shuoshuo.getTimestamp())));
        holder.time.setText(TimeUtil.timeDiff(System.currentTimeMillis(), shuoshuo.getTimestamp()));
        holder.content.setText(shuoshuo.getContent());
        holder.commentCount.setText(String.valueOf(shuoshuo.getCommentCount()) + "评论");
        holder.likedCount.setText(String.valueOf(shuoshuo.getLikedCount()));

        final String imgUrl = BmobImageDao.getSingedUrl(shuoshuo.getImgUrl());
        Picasso.with(ctx).load(imgUrl).placeholder(R.drawable.s_1).error(R.drawable.s_1).into(holder);
//        holder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                switchImgView(holder);
//                Intent intent = new Intent(ctx, ImgViewerActivity.class);
//                intent.putExtra("imgUrl", imgUrl);
//                ctx.startActivity(intent);
//            }
//        });
//            Picasso.with(ctx).load(imgUrl).into(holder.img, new Callback() {
//                @Override
//                public void onSuccess() {
//                    holder.img.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(ctx, ImgViewerActivity.class);
//                            intent.putExtra("imgUrl", imgUrl);
//                            ctx.startActivity(intent);
//                        }
//                    });
//                    holder.name.setTextColor(Color.WHITE);
//                    holder.address.setTextColor(Color.WHITE);
//                    holder.time.setTextColor(Color.WHITE);
//                    holder.content.setTextColor(Color.WHITE);
//                    holder.commentCount.setTextColor(Color.WHITE);
//                    holder.likedCount.setTextColor(Color.WHITE);
//                    holder.liked.setImageResource(R.drawable.wy_ic_card_like);
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });
//
//        if (imgUrl!=null) {
//            final View finalConvertView = convertView;
//            Picasso.with(ctx).load(imgUrl).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    finalConvertView.setBackgroundDrawable(new BitmapDrawable(bitmap));
//                    holder.name.setTextColor(Color.WHITE);
//                    holder.address.setTextColor(Color.WHITE);
//                    holder.time.setTextColor(Color.WHITE);
//                    holder.content.setTextColor(Color.WHITE);
//                    holder.commentCount.setTextColor(Color.WHITE);
//                    holder.likedCount.setTextColor(Color.WHITE);
//                    holder.liked.setImageResource(R.drawable.wy_ic_card_like);
//                    ShuoshuosAdapter.this.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//                /*设置背景色*/
//                    //1/2白色
//                    long n = shuoshuo.getTimestamp();
//                    if (n % 2 == 0) {
//                        finalConvertView.setBackgroundResource(R.drawable.s_1);
//                        holder.name.setTextColor(Color.BLACK);
//                        holder.address.setTextColor(Color.BLACK);
//                        holder.time.setTextColor(Color.BLACK);
//                        holder.content.setTextColor(Color.BLACK);
//                        holder.commentCount.setTextColor(Color.BLACK);
//                        holder.likedCount.setTextColor(Color.BLACK);
//                        holder.liked.setImageResource(R.drawable.wy_ic_card_like_grey);
//                    } else {
//                        //随机背景图[非白色]
//                        finalConvertView.setBackgroundResource(RandomBg.getBgResId(n));
//                        holder.name.setTextColor(Color.WHITE);
//                        holder.address.setTextColor(Color.WHITE);
//                        holder.time.setTextColor(Color.WHITE);
//                        holder.content.setTextColor(Color.WHITE);
//                        holder.commentCount.setTextColor(Color.WHITE);
//                        holder.likedCount.setTextColor(Color.WHITE);
//                        holder.liked.setImageResource(R.drawable.wy_ic_card_like);
//                    }
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            });
//        }
//

        //点主体时
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctx instanceof ShuoshuoDetailActivity) {
                    //如果是详情页
                    switchImgView(holder);
//                    Intent intent = new Intent(ctx, ImgViewerActivity.class);
//                    intent.putExtra("imgUrl", imgUrl);
//                    ctx.startActivity(intent);

//                    RongIM.getInstance().startConversation(ctx, Conversation.ConversationType.PRIVATE, shuoshuo.getUserId(), shuoshuo.getUserName());
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
//        long n = shuoshuo.getTimestamp();
//        if (n%2 == 0) {
//            convertView.setBackgroundResource(R.drawable.s_1);
//            holder.name.setTextColor(Color.BLACK);
//            holder.address.setTextColor(Color.BLACK);
//            holder.time.setTextColor(Color.BLACK);
//            holder.content.setTextColor(Color.BLACK);
//            holder.commentCount.setTextColor(Color.BLACK);
//            holder.likedCount.setTextColor(Color.BLACK);
//            holder.liked.setImageResource(R.drawable.wy_ic_card_like_grey);
//        } else {
//            //随机背景图[非白色]
//            convertView.setBackgroundResource(RandomBg.getBgResId(n));
//            holder.name.setTextColor(Color.WHITE);
//            holder.address.setTextColor(Color.WHITE);
//            holder.time.setTextColor(Color.WHITE);
//            holder.content.setTextColor(Color.WHITE);
//            holder.commentCount.setTextColor(Color.WHITE);
//            holder.likedCount.setTextColor(Color.WHITE);
//            holder.liked.setImageResource(R.drawable.wy_ic_card_like);
//        }

        return convertView;
    }

    boolean onlyhasImg = false;
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
