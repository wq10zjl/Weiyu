package com.syw.weiyu.ui.shuoshuo;

import android.media.Image;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

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
    private EditText et_comment;
    private String atUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_shuoshuo_detail);
        initViews();

        shuoshuo = (Shuoshuo) getIntent().getSerializableExtra("shuoshuo");
        if (shuoshuo!=null) {
            initShuoshuoDetailData(shuoshuo);
        } else {
            long ssId = getIntent().getLongExtra("ssId",-1);
            if (ssId == -1) {
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
                    Msger.e(ShuoshuoDetailActivity.this, msg);
                }
            });
        }
    }

    private void initShuoshuoDetailData(final Shuoshuo shuoshuo) {
        View shuoshuoView = getShuoshuoView(shuoshuo);
        View allCommentsTV = getAllCommentsTV();
        listView.addHeaderView(shuoshuoView);
        listView.addHeaderView(allCommentsTV);
        listView.setAdapter(adapter);

        final long ssId = shuoshuo.getId();
        Msger.i(this, "正在加载评论");
        WeiyuApi.get().getShuoshuoComments(ssId, new Listener<List<Comment>>() {
            @Override
            public void onSuccess(final List<Comment> data) {
                if (data.size() > 0) {
                    adapter.set(data);
                    Msger.i(ShuoshuoDetailActivity.this, "评论加载完成");

                    //回复添加@功能
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String atStr = "@" + data.get(position - 2).getUserName() + " ";
                            et_comment.setText(atStr);
                            et_comment.setSelection(atStr.length());
                            atUserId = data.get(position - 2).getUserId();
                        }
                    });
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
        ImageView chat = (ImageView) findViewById(R.id.header_right);
        chat.setImageResource(R.drawable.ic_action_chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startConversation(ShuoshuoDetailActivity.this, Conversation.ConversationType.PRIVATE, shuoshuo.getUserId(), shuoshuo.getUserName()+"("+shuoshuo.getUserGender()+")");
            }
        });

        listView = (ListView) findViewById(R.id.comments);
        adapter = new CommentsAdapter(ShuoshuoDetailActivity.this);

        et_comment = (EditText) findViewById(R.id.et_comment);
        ImageView btn_send = (ImageView) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏键盘
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ShuoshuoDetailActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                /*添加评论*/
                String content = et_comment.getText().toString();
                if (!content.contains("@")) atUserId = null;

                Msger.i(ShuoshuoDetailActivity.this, "正在添加评论...");
                WeiyuApi.get().addComment(shuoshuo, content, atUserId, new Listener<Comment>() {
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
