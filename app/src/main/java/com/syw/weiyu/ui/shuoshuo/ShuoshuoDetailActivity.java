package com.syw.weiyu.ui.shuoshuo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.syw.weiyu.R;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.WeiyuApi;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.ui.adapter.CommentsAdapter;
import com.syw.weiyu.util.StringUtil;
import com.syw.weiyu.util.Toaster;

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

        Shuoshuo shuoshuo = (Shuoshuo) getIntent().getSerializableExtra("shuoshuo");
        setShuoshuo(shuoshuo);

        final ListView commentsListView = (ListView) findViewById(R.id.comments);
        final CommentsAdapter adapter = new CommentsAdapter(ShuoshuoDetailActivity.this);
        final long ssId = shuoshuo.getId();
        Toaster.i(this, "正在加载评论");
        WeiyuApi.get().getShuoshuoComments(ssId, new Listener<List<Comment>>() {
            @Override
            public void onCallback(@NonNull CallbackType callbackType, @Nullable List<Comment> data, @Nullable String msg) {
                adapter.set(data);
                commentsListView.setAdapter(adapter);
            }
        });

        final EditText et_comment = (EditText) findViewById(R.id.et_comment);
        ImageView btn_send = (ImageView) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_comment.getText().toString();
                if (!StringUtil.isEmpty(content)) {
                    WeiyuApi.get().addComment(ssId, content, new Listener<Comment>() {
                        @Override
                        public void onCallback(@NonNull CallbackType callbackType, @Nullable Comment data, @Nullable String msg) {
                            if (callbackType == CallbackType.onSuccess) {
                                Toaster.i(ShuoshuoDetailActivity.this, msg);
                                adapter.append(data);
                            }
                            else Toaster.e(ShuoshuoDetailActivity.this,msg);
                        }
                    });
                }
            }
        });
    }

    private void setShuoshuo(Shuoshuo shuoshuo) {
        TextView name = (TextView) findViewById(R.id.shuoshuo_tv_name);
        TextView time = (TextView) findViewById(R.id.shuoshuo_tv_time);
        TextView content = (TextView) findViewById(R.id.shuoshuo_tv_content);
        TextView address = (TextView) findViewById(R.id.shuoshuo_tv_address);
//        TextView comment_count = (TextView) findViewById(R.id.shuoshuo_tv_comment_count);

        name.setText(shuoshuo.getUserName());
        time.setText(new SimpleDateFormat("MM-dd kk:mm").format(new Date(shuoshuo.getTimestamp())));
        content.setText(shuoshuo.getContent());
        address.setText(shuoshuo.getLocation().getProvince()+shuoshuo.getLocation().getCity()+shuoshuo.getLocation().getDistrict());
//        comment_count.setText
    }
}
