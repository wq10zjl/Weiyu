package com.syw.weiyu.ui.main.explore;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.paging.listview.PagingListView;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.api.WeiyuApi;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.R;
import com.syw.weiyu.ui.adapter.NearByUserAdapter;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.util.ACache;
import com.syw.weiyu.util.WeiyuAppUpdateRemind;

import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.rong.imkit.RongIM;

/**
 * Created by youwei on 2015/2/9.
 */
public class NearByUserActivity extends FragmentActivity {

    //用于视图适配器的mapList
    List<HashMap<String, String>> usermapList = new ArrayList<>();

    int pageIndex = 0;
    int totalPage = 0;
    NearByUserAdapter.LOADTYPE loadType;
    /**
     * 设置加载类型
     * @param loadType
     */
    public void setLoadType(NearByUserAdapter.LOADTYPE loadType) {
        this.loadType = loadType;
    }
    PagingListView listView;
    NearByUserAdapter adapter;
    PtrClassicFrameLayout mPtrFrame;

    //LBS callback
    AjaxCallBack<String> lbsCloudSearchCallback = new LBSCloudSearchCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_nearbyuser);
        findViewById(R.id.header_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        initHeader();
        initUltraPullToRefresh();
        initListView();

        //check update
        new WeiyuAppUpdateRemind(this,this.getString(R.string.adsmogo_appid)).checkUpdate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Weiyu","NearByUserActivity onResume");

        //读取缓存的lists
        usermapList = AppContext.getInstance().getUsermapList();
        //若空，则刷新数据（延迟一秒），否者直接设置适配器
        if(usermapList.isEmpty()) {
            new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    mPtrFrame.autoRefresh();
                }
            }.sendEmptyMessageDelayed(1,500);
        } else {
            adapter.setData(usermapList);
        }

        //add banner ad
        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(WeiyuApi.get().getBannerAdView(this, null));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                //显示feeds广告
//                showFeedsAd();

                //刷新列表数据
//                updateListData(dataType);
                pageIndex = 0;
                setLoadType(NearByUserAdapter.LOADTYPE.TYPE_REFRESH);
                LBSCloud.getInstance().nearbyUserSearch(0, lbsCloudSearchCallback);
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
        listView = (PagingListView) findViewById(R.id.lv_nearby);
        adapter = new NearByUserAdapter(this);
        listView.setAdapter(adapter);
        listView.setHasMoreItems(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIM.getInstance().startPrivateChat(
                        NearByUserActivity.this,
                        adapter.getData().get(position-1).get("userId"),
                        adapter.getData().get(position-1).get("name"));
            }
        });

        listView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (pageIndex+1 < totalPage) {
                    setLoadType(NearByUserAdapter.LOADTYPE.TYPE_MORE);
                    LBSCloud.getInstance().nearbyUserSearch(++pageIndex, lbsCloudSearchCallback);
                } else {
                    listView.onFinishLoading(false, null);
                }
            }
        });
    }

    /**
     * LBS云检索回调
     */
    class LBSCloudSearchCallback extends AjaxCallBack<String> {

        private List<HashMap<String,String>> newDataList = new ArrayList<>();

        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(String s) {
            Log.d("Weiyu", " LBSCloud poi search return:" + s);
            //解析数据并存入
            JSONObject result = JSON.parseObject(s);
            if (result.getString("status").equals("0")) {
                JSONArray poiArray = result.getJSONArray("contents");
                newDataList.clear();
                for (int i=0; i<poiArray.size(); i++) {
                    JSONObject poi = poiArray.getJSONObject(i);

                    //map for adapter
                    HashMap<String, String> map = new HashMap<>();
                    map.put("userId", poi.getString("userId"));
                    map.put("gender", poi.getString("gender"));
                    map.put("name", poi.getString("name"));
                    map.put("address", poi.getString("address"));
                    newDataList.add(map);

                    //users(only has userId&name&gender)
                    User user = new User();
                    user.setId(poi.getString("userId"));
                    user.setName(poi.getString("name"));
                    user.setGender(poi.getString("gender"));
                    //save userId-json[k-v value] cache
                    ACache.get(NearByUserActivity.this).put(user.getId(), JSON.toJSONString(user));
                }
                //set totalPage
                if (totalPage == 0) {
                    totalPage = (int)Math.ceil(Double.parseDouble(result.getString("total"))/Double.parseDouble(result.getString("size")));
                }

                //设置适配器
                if (loadType == NearByUserAdapter.LOADTYPE.TYPE_REFRESH) {
                    //结束下拉刷新
                    mPtrFrame.refreshComplete();
                    adapter.setData(newDataList);
                    //set has more page
                    listView.setHasMoreItems(pageIndex + 1 < totalPage);
                    //save result lists into ram
                    AppContext.getInstance().setUsermapList(newDataList);
                } else if (loadType == NearByUserAdapter.LOADTYPE.TYPE_MORE){
                    adapter.addData(newDataList);
                    listView.onFinishLoading(pageIndex + 1 < totalPage, null);
                    //save result lists into ram
                    AppContext.getInstance().setUsermapList(adapter.getData());
                }

            }
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            Log.d("Weiyu", " LBSCloud poi search failure:" + strMsg);
            //结束下拉刷新
            mPtrFrame.refreshComplete();
        }
    }
}
