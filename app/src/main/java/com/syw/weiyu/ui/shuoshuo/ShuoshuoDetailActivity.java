package com.syw.weiyu.ui.shuoshuo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.syw.weiyu.R;
import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.ui.adapter.CommentsAdapter;
import com.syw.weiyu.ui.adapter.ShuoshuosAdapter;
import com.syw.weiyu.util.StringUtil;
import com.syw.weiyu.util.Msger;

import java.util.ArrayList;
import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-30
 * desc: 说说详情页
 */
public class ShuoshuoDetailActivity extends FragmentActivity {

    private Shuoshuo shuoshuo;
    private CommentsAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_shuoshuo_detail);
        initViews();

        shuoshuo = (Shuoshuo) getIntent().getSerializableExtra("shuoshuo");
        if (shuoshuo!=null) {
            initShuoshuoDetailData(shuoshuo);
        } else {
            String ssId = getIntent().getStringExtra("ssId");
            if (StringUtil.isEmpty(ssId)) {
                Msger.e(this,"说说不存在");
                return;
            }

            Msger.i(this, "正在加载说说");
            WeiyuApi.get().getShuoshuo(ssId, new Listener<Shuoshuo>() {
                @Override
                public void onSuccess(Shuoshuo data) {
                    initShuoshuoDetailData(data);
                }

                @Override
                public void onFailure(String msg) {
                    Msger.e(ShuoshuoDetailActivity.this, "加载说说出错");
                }
            });
        }
    }

    private void initShuoshuoDetailData(Shuoshuo shuoshuo) {
        View shuoshuoView = getShuoshuoView(shuoshuo);
        View allCommentsTV = getAllCommentsTV();
        listView.addHeaderView(shuoshuoView);
        listView.addHeaderView(allCommentsTV);
        listView.setAdapter(adapter);

        final long ssId = shuoshuo.getId();
        Msger.i(this, "正在加载评论");
        WeiyuApi.get().getShuoshuoComments(ssId, new Listener<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> data) {
                if (data.size() > 0) {
                    adapter.set(data);
                    Msger.i(ShuoshuoDetailActivity.this, "评论加载完成");
                } else {
                    Msger.e(ShuoshuoDetailActivity.this, "暂无评论");
                }
            }

            @Override
            public void onFailure(String msg) {
                Msger.e(ShuoshuoDetailActivity.this, "评论加载出错:" + msg);
            }
        });
    }

    private void initViews() {
        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tvTitle = (TextView) findViewById(R.id.header_title);
        tvTitle.setText("详情");

        listView = (ListView) findViewById(R.id.comments);
        adapter = new CommentsAdapter(ShuoshuoDetailActivity.this);

        final EditText et_comment = (EditText) findViewById(R.id.et_comment);
        ImageView btn_send = (ImageView) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏键盘
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ShuoshuoDetailActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                String content = et_comment.getText().toString();
                Msger.i(ShuoshuoDetailActivity.this, "正在添加评论...");
                if (!StringUtil.isEmpty(content)) {
                    WeiyuApi.get().addComment(shuoshuo, content, new Listener<Comment>() {
                        @Override
                        public void onSuccess(Comment data) {
                            Msger.i(ShuoshuoDetailActivity.this, "评论成功");
                            //添加adapter里的数据
                            adapter.append(data);
                            //评论数++
                            ((TextView) findViewById(R.id.shuoshuo_tv_comment_count)).setText(String.valueOf(shuoshuo.getCommentCount() + 1));
                            //定位到底部
                            listView.smoothScrollToPosition(listView.getBottom());
                            //清空输入框
                            et_comment.setText("");
                        }

                        @Override
                        public void onFailure(String msg) {
                            Msger.e(ShuoshuoDetailActivity.this, "评论出错啦:" + msg);
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
        ShuoshuosAdapter adapter = new ShuoshuosAdapter(this);
        List<Shuoshuo> list = new ArrayList<>();
        list.add(shuoshuo);
        adapter.set(list);
        return adapter.getView(0,null,null);
    }
}
