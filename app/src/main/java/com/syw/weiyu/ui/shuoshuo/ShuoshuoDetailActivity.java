package com.syw.weiyu.ui.shuoshuo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.syw.weiyu.R;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.WeiyuApi;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.ui.adapter.CommentsAdapter;
import com.syw.weiyu.util.StringUtil;
import com.syw.weiyu.util.Msger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-30
 * desc: 说说详情页
 */
public class ShuoshuoDetailActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_shuoshuo_detail);

        final ListView listView = (ListView) findViewById(R.id.comments);

        Shuoshuo shuoshuo = (Shuoshuo) getIntent().getSerializableExtra("shuoshuo");
        View shuoshuoView = getShuoshuoView(shuoshuo);
        View allCommentsTV = getAllCommentsTV();
        listView.addHeaderView(shuoshuoView);
        listView.addHeaderView(allCommentsTV);

        final CommentsAdapter adapter = new CommentsAdapter(ShuoshuoDetailActivity.this);
        final long ssId = shuoshuo.getId();
        Msger.i(this, "正在加载评论");
        WeiyuApi.get().getShuoshuoComments(ssId, new Listener<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> data) {
                adapter.set(data);
                listView.setAdapter(adapter);
                Msger.i(ShuoshuoDetailActivity.this, "评论加载完成");
            }

            @Override
            public void onFailure(String msg) {
                Msger.e(ShuoshuoDetailActivity.this, "评论加载出错:"+msg);
            }
        });

        final EditText et_comment = (EditText) findViewById(R.id.et_comment);
        ImageView btn_send = (ImageView) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_comment.getText().toString();
                Msger.i(ShuoshuoDetailActivity.this,"正在添加评论...");
                if (!StringUtil.isEmpty(content)) {
                    WeiyuApi.get().addComment(ssId, content, new Listener<Comment>() {
                        @Override
                        public void onSuccess(Comment data) {
                            adapter.append(data);
                            Msger.i(ShuoshuoDetailActivity.this, "评论成功");
                        }
                        @Override
                        public void onFailure(String msg) {
                            Msger.e(ShuoshuoDetailActivity.this, "评论出错啦:"+msg);
                        }
                    });
                }
            }
        });
    }

    private View getAllCommentsTV() {
        return LayoutInflater.from(this).inflate(R.layout.include_tv_all_comments,null);
    }

    private View getShuoshuoView(Shuoshuo shuoshuo) {
        View shuoshuoView = LayoutInflater.from(this).inflate(R.layout.wy_shuoshuo_lv_item,null);

        TextView name = (TextView) shuoshuoView.findViewById(R.id.shuoshuo_tv_name);
        TextView time = (TextView) shuoshuoView.findViewById(R.id.shuoshuo_tv_time);
        TextView content = (TextView) shuoshuoView.findViewById(R.id.shuoshuo_tv_content);
        TextView address = (TextView) shuoshuoView.findViewById(R.id.shuoshuo_tv_address);
//        TextView comment_count = (TextView) findViewById(R.id.shuoshuo_tv_comment_count);

        name.setText(shuoshuo.getUserName());
        time.setText(new SimpleDateFormat("MM-dd kk:mm").format(new Date(shuoshuo.getTimestamp())));
        content.setText(shuoshuo.getContent());
        address.setText(shuoshuo.getLocation().getProvince()+shuoshuo.getLocation().getCity()+shuoshuo.getLocation().getDistrict());
//        comment_count.setText


        /*设置背景色*/
        //1/2白色
        if (shuoshuo.getTimestamp()%2 == 0) {
            shuoshuoView.setBackgroundResource(R.drawable.s_1);
            name.setTextColor(Color.BLACK);
            address.setTextColor(Color.BLACK);
            time.setTextColor(Color.BLACK);
            content.setTextColor(Color.BLACK);
        } else {
            //随机背景图
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
            shuoshuoView.setBackgroundResource(bgs[i]);
            name.setTextColor(Color.WHITE);
            address.setTextColor(Color.WHITE);
            time.setTextColor(Color.WHITE);
            content.setTextColor(Color.WHITE);
        }

        return shuoshuoView;
    }
}
