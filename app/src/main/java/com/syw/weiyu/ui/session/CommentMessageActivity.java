package com.syw.weiyu.ui.session;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Comment;
import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

public class CommentMessageActivity extends FragmentActivity {

    ListView commentMessageListView;
    List<Comment> data = new ArrayList<>();

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
    }

    private void initView() {
        findViewById(R.id.header_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        commentMessageListView = (ListView) findViewById(R.id.lv_message_comment);
    }

}
