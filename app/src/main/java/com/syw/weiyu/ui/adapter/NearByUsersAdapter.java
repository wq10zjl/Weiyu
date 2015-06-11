package com.syw.weiyu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paging.listview.PagingBaseAdapter;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.Null;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.util.Msger;
import com.syw.weiyu.util.TimeUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by songyouwei on 2015/4/27.
 */
public class NearByUsersAdapter extends PagingBaseAdapter {

    public void set(List<User> data) {
        users.clear();
        users.addAll(data);
        notifyDataSetChanged();
    }

    public void append(List<User> data) {
        users.addAll(data);
        notifyDataSetChanged();
    }

    public List<User> getData() {
        return users;
    }

    LayoutInflater mInflater;
    Context ctx;
    List<User> users = new ArrayList<>();
//
    public NearByUsersAdapter(Context ctx) {
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    //在外面先定义，ViewHolder静态类
    static class ViewHolder {
        public ImageView portrait;
        public TextView name;
        public TextView address;
        public TextView time;
        public ImageView hi;
    }

//    @Override
    public int getCount() {
        return users.size();
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.wy_nearbyuser_lv_item, null);
            holder = new ViewHolder();
            holder.portrait = (ImageView) convertView.findViewById(R.id.nearbyuser_iv_portrait);
            holder.name = (TextView) convertView.findViewById(R.id.nearbyuser_tv_name);
            holder.address = (TextView) convertView.findViewById(R.id.nearbyuser_tv_address);
            holder.time = (TextView) convertView.findViewById(R.id.nearbyuser_tv_time);
            holder.hi = (ImageView) convertView.findViewById(R.id.nearbyuser_iv_hi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final User user = users.get(position);
        holder.portrait.setImageResource(
                user.getGender().equals("男") ?
                        R.drawable.wy_icon_male :
                        R.drawable.wy_icon_female
        );
        holder.name.setText(user.getName());
        holder.address.setText(user.getLocation()==null?user.getAddressStr():user.getLocation().getAddress());
        long lastUpdateTime = user.getLastOnlineTimestamp();
        if (lastUpdateTime!=0)holder.time.setText(TimeUtil.timeDiff(System.currentTimeMillis(),lastUpdateTime));
        else {
            long updatedAt = TimeUtil.parse2Timestamp(user.getUpdatedAt());
            if (updatedAt!=0) holder.time.setText(TimeUtil.timeDiff(System.currentTimeMillis(),updatedAt));
            else holder.time.setVisibility(View.INVISIBLE);
        }
        holder.hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeiyuApi.get().sayHi(user.getId(), new Listener<Null>() {
                    @Override
                    public void onSuccess(Null data) {
                        Msger.i((Activity)ctx, "打招呼消息发送完成");
                        holder.hi.setImageResource(R.drawable.ic_action_hi_completed);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Msger.e((Activity)ctx, "打招呼出错了:"+msg);
                    }
                });
            }
        });

        //点击用户时开启私聊
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startConversation(ctx, Conversation.ConversationType.PRIVATE, user.getId(), user.getName());
            }
        });

        return convertView;
    }
}
