package com.syw.weiyu.ui.session;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.ui.adapter.CommentMessageAdapter;
import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

public class CommentMessageActivity extends FragmentActivity {

    ListView commentMessageListView;
    List<Comment> data = new ArrayList<>();
    CommentMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_message_comment);

        initView();
        initData();
    }

    private void initData() {
        FinalDb finalDb = FinalDb.create(this);
        data = finalDb.findAll(Comment.class);
        adapter.set(data);
    }

    private void initView() {
        ((TextView)findViewById(R.id.header_title)).setText("评论消息");
        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView clean = (ImageView) findViewById(R.id.header_right);
        clean.setImageResource(R.drawable.ic_action_clean);
        clean.setPadding(10,10,10,10);
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentMessageActivity.this,AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("清空消息列表")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FinalDb.create(CommentMessageActivity.this).deleteAll(Comment.class);
                                adapter.set(new ArrayList<Comment>());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();
            }
        });

        commentMessageListView = (ListView) findViewById(R.id.lv_message_comment);
        adapter = new CommentMessageAdapter(this);
        commentMessageListView.setAdapter(adapter);
    }

}
