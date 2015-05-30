package com.syw.weiyu.ui.shuoshuo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import com.syw.weiyu.R;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.WeiyuApi;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.bean.Shuoshuo;

import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-30
 * desc:
 */
public class ShuoshuoDetailActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.id.);
        Shuoshuo shuoshuo = (Shuoshuo) getIntent().getSerializableExtra("shuoshuo");
        long ssId = shuoshuo.getId();
        WeiyuApi.get().getShuoshuoComments(ssId, new Listener<List<Comment>>() {
            @Override
            public void onCallback(@NonNull CallbackType callbackType, @Nullable List<Comment> data, @Nullable String msg) {

            }
        });
    }
}
