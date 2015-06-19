package com.syw.weiyu.ui.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.paging.listview.PagingListView;
import com.syw.weiyu.core.AppException;
import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.Null;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.ShuoshuoList;
import com.syw.weiyu.ui.adapter.ShuoshuosAdapter;

import com.syw.weiyu.util.Msger;

import com.syw.weiyu.util.StringUtil;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 用户个人主页
 */
public class UserHomeActivity extends Activity {

    int pageIndex = 0;
    int totalPage = 0;

    PagingListView listView;
    ShuoshuosAdapter adapter;

    PtrClassicFrameLayout mPtrFrame;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_shuoshuo);
        userId = getIntent().getStringExtra("userId");
        if (StringUtil.isEmpty(userId)) try {
            userId = WeiyuApi.get().getAccount().getId();
        } catch (AppException e) {
            Msger.i(this,e.getMessage());
            return;
        }

        initViews();
        initUltraPullToRefresh();
        initListView();

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mPtrFrame.autoRefresh();
            }
        }.sendEmptyMessageDelayed(1, 500);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //create banner ad
        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(WeiyuApi.get().getBannerAdView(this, null));
        }
    }

    @Override
    protected void onDestroy() {
        WeiyuApi.get().onBannerDestory();
        super.onDestroy();
    }

    private void initViews() {
        final ImageView leftHeader = (ImageView) findViewById(R.id.header_right);
        try {
            if (userId.equals(WeiyuApi.get().getAccount().getId())) {
                ((TextView) findViewById(R.id.header_title)).setText("我的说说");
            } else {
                leftHeader.setImageResource(R.drawable.ic_action_hi);
                leftHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WeiyuApi.get().sayHi(userId, new Listener<Null>() {
                            @Override
                            public void onSuccess(Null data) {
                                Msger.i(UserHomeActivity.this, "TA收到了你的打招呼");
                                leftHeader.setImageResource(R.drawable.ic_action_hi_completed);
                                leftHeader.setOnClickListener(null);
                            }

                            @Override
                            public void onFailure(String msg) {
                                Msger.e(UserHomeActivity.this, "打招呼出错了:"+msg);
                            }
                        });
                    }
                });
                ((TextView) findViewById(R.id.header_title)).setText("TA的说说");
            }
        } catch (AppException e) {
            e.printStackTrace();
        }
        ImageView btnBack = (ImageView) findViewById(R.id.header_left);
        btnBack.setImageResource(R.drawable.ic_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    /**
     * 初始化下拉刷新框架
     */
    private void initUltraPullToRefresh() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                WeiyuApi.get().getUserShuoshuo(userId, pageIndex, new Listener<ShuoshuoList>() {
                    @Override
                    public void onSuccess(ShuoshuoList data) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        adapter.set(data.getShuoshuos());
                        //set totalPage
                        totalPage = (int) Math.ceil((double) data.getTotal() / (double) data.getShuoshuos().size());
                        //set has more page
                        listView.setHasMoreItems(pageIndex < totalPage);
                    }

                    @Override
                    public void onFailure(String msg) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        Msger.e(UserHomeActivity.this, msg);
                    }
                });
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    /**
     * 初始化列表视图
     */
    private void initListView() {
        listView = (PagingListView) findViewById(R.id.lv_shuoshuo);
        adapter = new ShuoshuosAdapter(this);
        listView.setAdapter(adapter);
        listView.setHasMoreItems(false);

        listView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                WeiyuApi.get().getUserShuoshuo(userId, ++pageIndex, new Listener<ShuoshuoList>() {
                    @Override
                    public void onSuccess(ShuoshuoList data) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        adapter.append(data.getShuoshuos());
                        //set has more page
                        listView.onFinishLoading(pageIndex < totalPage, null);
                    }

                    @Override
                    public void onFailure(String msg) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        Msger.e(UserHomeActivity.this, msg);
                        pageIndex--;
                    }
                });
            }
        });
    }

}