package com.syw.weiyu.ui.main.explore;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.paging.listview.PagingListView;
import com.syw.weiyu.R;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.api.WeiyuApi;
import com.syw.weiyu.bean.UserList;
import com.syw.weiyu.ui.adapter.NearByUsersAdapter;
import com.syw.weiyu.util.Msger;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by youwei on 2015/2/9.
 */
public class NearByUserActivity extends FragmentActivity {
    int pageIndex = 0;
    int totalPage = 0;

    PagingListView listView;
    NearByUsersAdapter adapter;

    PtrClassicFrameLayout mPtrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_nearbyuser);
        initViews();
        initUltraPullToRefresh();
        initListView();
    }


    @Override
    protected void onResume() {
        super.onResume();

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                WeiyuApi.get().getNearbyUsers(1, new Listener<UserList>() {
                    @Override
                    public void onSuccess(UserList data) {
                        adapter.set(data.getUsers());
                    }

                    @Override
                    public void onFailure(String msg) {

                    }
                });
            }
        }.sendEmptyMessageDelayed(1, 500);

        //add banner ad
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
        TextView tvTitle = (TextView) findViewById(R.id.header_tv_title);
        tvTitle.setText("附近的人");
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
                pageIndex = 0;
                WeiyuApi.get().getNearbyUsers(0, new Listener<UserList>() {
                    @Override
                    public void onSuccess(UserList data) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        adapter.set(data.getUsers());
                        //set totalPage
                        if (totalPage == 0) {
                            totalPage = (int) Math.ceil(data.getTotal() / data.getUsers().size());
                        }
                        //set has more page
                        listView.setHasMoreItems(pageIndex + 1 < totalPage);
                    }

                    @Override
                    public void onFailure(String msg) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        Msger.e(NearByUserActivity.this, msg);
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
        listView = (PagingListView) findViewById(R.id.lv_nearby_users);
        adapter = new NearByUsersAdapter(this);
        listView.setAdapter(adapter);
        listView.setHasMoreItems(false);

        listView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (pageIndex + 1 < totalPage) {
                    WeiyuApi.get().getNearbyUsers(++pageIndex, new Listener<UserList>() {
                        @Override
                        public void onSuccess(UserList data) {
                            //结束下拉刷新
                            mPtrFrame.refreshComplete();
                            adapter.append(data.getUsers());
                            //set has more page
                            listView.onFinishLoading(pageIndex + 1 < totalPage, null);
                        }

                        @Override
                        public void onFailure(String msg) {
                            //结束下拉刷新
                            mPtrFrame.refreshComplete();
                            Msger.e(NearByUserActivity.this, msg);
                            pageIndex--;
                        }
                    });
                } else {
                    listView.onFinishLoading(false, null);
                }
            }
        });
    }
}
