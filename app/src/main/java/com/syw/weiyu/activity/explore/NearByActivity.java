package com.syw.weiyu.activity.explore;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

//import com.qq.e.ads.AdListener;
//import com.qq.e.ads.AdRequest;
//import com.qq.e.ads.AdSize;
//import com.qq.e.ads.AdView;
//import com.baidu.mobads.AdView;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.LBS.LBSCloud;
import com.syw.weiyu.R;
import com.syw.weiyu.entity.User;
import com.syw.weiyu.util.ACache;

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
 * Created by songyouwei on 2015/2/9.
 */
public class NearByActivity extends FragmentActivity {

    //用于视图适配器的mapList
    List<HashMap<String, Object>> usermapList = new ArrayList<>();
    //这个UserList只放userId&name，用于开启聊天
    List<User> userList = new ArrayList<>();

    ListView listView;
    SimpleAdapter adapter;

    //数据类型
//    private int dataType = TYPE_NEARBY;//默认为附近
//    private static final int TYPE_LOCAL = 0;
//    private static final int TYPE_NEARBY = 1;

    PtrClassicFrameLayout mPtrFrame;

    //豌豆荚banner广告
//    private AdBanner adBanner;
//    private View adBannerView;

    //Baidu Banner Ad
//    private AdView adView;
//    private RelativeLayout l;

    //LBS callback
    AjaxCallBack<String> lbsCloudSearchCallback = new LBSCloudSearchCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_nearby);
        findViewById(R.id.header_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        initHeader();
        initUltraPullToRefresh();
        initListView();

//        initBaiduBannerAd();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Weiyu","NearByActivity onResume");

        //读取缓存的lists
        usermapList = AppContext.getInstance().getUsermapList();
        userList = AppContext.getInstance().getUserList();
        //若空，则刷新数据（延迟一秒），否者直接设置适配器
        if(usermapList.isEmpty() || userList.isEmpty()) {
            new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    mPtrFrame.autoRefresh();
                }
            }.sendEmptyMessageDelayed(1,500);
        } else {
            setListViewAdapter();
        }

        //显示banner广告
//        Log.d("Weiyu","showBannerAd");
//        showBaiduMobAdBannerAd();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


//    /**
//     * 初始化百度Banner广告
//     */
//    private void initBaiduBannerAd() {
//        l = (RelativeLayout)findViewById(R.id.banner_ad_container);
//        adView = new AdView(this);
//    }
//
//    /**
//     * 显示百度Banner广告
//     */
//    private void showBaiduMobAdBannerAd() {
//        l.removeAllViews();
//        l.addView(adView);
//    }


//    /**
//     * 筹备信息流顶部广告（多盟）
//     */
//    private void prepareFeedsAd() {
//        feedsAdView = new FeedsAdView(this,this.getString(R.string.domob_publisher_id),this.getString(R.string.domob_adid_feeds));
//        feedsAdView.loadFeedsAd();
//        LinearLayout feedsAdContainer = (LinearLayout) findViewById(R.id.ad_container);
//        feedsAdContainer.addView(feedsAdView,0);
//        feedsAdView.setFeedsAdListener(new FeedsAdListener() {
//            @Override
//            public void onFeedsAdReady() {
//                Log.d("Weiyu","onFeedsAdReady");
//            }
//
//            @Override
//            public void onFeedsAdFailed(AdManager.ErrorCode errorCode) {
//                Log.d("Weiyu","onFeedsAdFailed,errorCode:"+errorCode);
//            }
//
//            @Override
//            public void onFeedsAdPresent() {
//                Log.d("Weiyu","onFeedsAdPresent");
//            }
//
//            @Override
//            public void onFeedsAdDismiss() {
//                Log.d("Weiyu","onFeedsAdDismiss");
//                feedsAdView.loadFeedsAd();
//            }
//
//            @Override
//            public void onLandingPageOpen() {
//                Log.d("Weiyu","onLandingPageOpen");
//            }
//
//            @Override
//            public void onLandingPageClose() {
//                Log.d("Weiyu","onLandingPageClose");
//            }
//
//            @Override
//            public void onFeedsAdLeaveApplication() {
//                Log.d("Weiyu","onFeedsAdLeaveApplication");
//            }
//
//            @Override
//            public void onFeedsAdClicked(FeedsAdView feedsAdView) {
//                Log.d("Weiyu","onFeedsAdClicked");
//            }
//        });
//    }
//
//    /**
//     * 显示信息流顶部广告（如果可用）,如果不可用则后台加载
//     */
//    private void showFeedsAd() {
//        if (feedsAdView.isFeedsAdReady()) {
//            feedsAdView.showFeedsAd(this);
//        } else {
//            feedsAdView.loadFeedsAd();
//        }
//    }

    /**
     * 显示Banner广告（腾讯广点通）
     */
//    private void showGDTBannerAd() {
//        RelativeLayout l = (RelativeLayout)findViewById(R.id.banner_ad_container);
//// 创建Banner广告AdView对象
//// appId : 在 http://e.qq.com/dev/ 能看到的app唯一字符串
//// posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
//        AdView adv = new AdView(this, AdSize.BANNER, getString(R.string.gdt_app_id),getString(R.string.gdt_adid_banner));
//        l.addView(adv);
//// 广告请求数据，可以设置广告轮播时间，默认为30s
//        AdRequest adr = new AdRequest();
////设置广告刷新时间，为30~120之间的数字，单位为s,0标识不自动刷新
//        adr.setRefresh(30);
////在banner广告上展示关闭按钮
//        adr. setShowCloseBtn(true);
////设置空广告和首次收到广告数据回调
////调用fetchAd方法后会发起广告请求 */
//        adv.setAdListener(new AdListener() {
//
//            @Override
//            public void onNoAd() {
//                Log.i("Weiyu:","Banner AD LoadFail");
//            }
//
//            @Override
//            public void onBannerClosed() {
//                //仅在开启广点通广告关闭按钮时生效
//                Log.i("Weiyu:","Banner AD Closed");
//            }
//
//            @Override
//            public void onAdReceiv() {
//                Log.i("Weiyu:","Banner AD Ready to show");
//            }
//
//            @Override
//            public void onAdExposure() {
//                Log.i("Weiyu:","Banner AD Exposured");
//            }
//
//            @Override
//            public void onAdClicked() {
//                //Banner广告发生点击时回调，由于点击去重等因素不能保证回调数量与联盟最终统计数量一致
//                Log.i("Weiyu:","Banner AD Clicked");
//            }
//        });
///* 发起广告请求，收到广告数据后会展示数据 */
//        adv.fetchAd(adr);
//    }


    /**
     * 显示豌豆荚Banner广告
     */
//    private void showWandoujiaBannerAd() {
//        ViewGroup containerView = (ViewGroup) findViewById(R.id.banner_ad_container);
//        if (adBannerView != null && containerView.indexOfChild(adBannerView) >= 0) {
//            containerView.removeView(adBannerView);
//        }
//        adBanner = Ads.showBannerAd(this, (ViewGroup) findViewById(R.id.banner_ad_container),
//                getString(R.string.wdj_adid_banner),new AdLoadFailerListener() {
//                    @Override
//                    public void onLoadFailure() {
//                        Log.d("Weiyu","Wandoujia Banner AdLoadFail");
//                    }
//                });
//        adBannerView = adBanner.getView();
//    }



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
                LBSCloud.getInstance().nearbySearch(lbsCloudSearchCallback);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

//    /**
//     * 更新列表数据
//     * @param type 数据类型
//     */
//    private void updateListData(int type) {
//        switch (type) {
//            case TYPE_LOCAL:
//                LBSCloud.getInstance().localSearch(lbsCloudSearchCallback);
//                break;
//            case TYPE_NEARBY:
//                LBSCloud.getInstance().nearbySearch(lbsCloudSearchCallback);
//                break;
//        }
//    }

    /**
     * 初始化列表视图
     */
    private void initListView() {
        listView = (ListView) findViewById(R.id.lv_nearby);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);
                RongIM.getInstance().startPrivateChat(NearByActivity.this, user.getUserId(), user.getName());
            }
        });
    }

    /**
     * 初始化头部视图（已作废，换成只显示附近）
     */
//    private void initHeader() {
//        //切换按钮
//        SwitcherButton switcherButton = (SwitcherButton) findViewById(R.id.header_sb_rightview_switcherbutton);
//        //游标
//        final LinearLayout switcherTab = (LinearLayout) findViewById(R.id.switcher_layout_tab);
//
//        //设置切换按钮文本
//        TextView leftTextView = (TextView) findViewById(R.id.switcher_tv_left_text);
//        TextView rightTextView = (TextView) findViewById(R.id.switcher_tv_right_text);
//        leftTextView.setText("同城");
//        rightTextView.setText("附近");
//
//        //监听切换按钮
//        switcherButton.setOnSwitcherButtonClickListener(new SwitcherButton.onSwitcherButtonClickListener() {
//            @Override
//            public void onClick(SwitcherButton.SwitcherButtonState state) {
//                FragmentTransaction ft = getSupportFragmentManager()
//                        .beginTransaction();
//                ft.setCustomAnimations(R.anim.fragment_fadein,
//                        R.anim.fragment_fadeout);
//                switch (state) {
//                    case LEFT:
//                        //左边是“同城”
//                        switcherTab.setGravity(Gravity.LEFT);
//                        dataType = TYPE_LOCAL;
//                        mPtrFrame.autoRefresh();
//                        break;
//
//                    case RIGHT:
//                        //右边是“附近”
//                        switcherTab.setGravity(Gravity.RIGHT);
//                        dataType = TYPE_NEARBY;
//                        mPtrFrame.autoRefresh();
//                        break;
//                }
//            }
//        });
//    }

    /**
     * 设置列表视图适配器
     */
    private void setListViewAdapter() {
        //set adapter
        adapter = new SimpleAdapter(
                NearByActivity.this,
                usermapList,
                R.layout.wy_nearby_lv_item,
                new String[]{"lv_icon_portrait","lv_tv_name", "lv_tv_info"},
                new int[]{ R.id.lv_icon_portrait,R.id.lv_tv_name, R.id.lv_tv_aboutinfo});
        listView.setAdapter(adapter);
    }

    /**
     * LBS云检索回调
     */
    class LBSCloudSearchCallback extends AjaxCallBack<String> {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(String s) {
            Log.d("Weiyu", " LBSCloud poi search return:" + s);
            //结束下拉刷新
            mPtrFrame.refreshComplete();
            //解析数据并存入
            JSONObject result = JSON.parseObject(s);
            if (result.getString("status").equals("0")) {
                //clear this two lists
                usermapList.clear();
                userList.clear();

                JSONArray poiArray = result.getJSONArray("contents");
                for (int i=0; i<poiArray.size(); i++) {
                    JSONObject poi = poiArray.getJSONObject(i);

                    //map for adapter
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("lv_icon_portrait", poi.getString("gender").equals("男")?R.drawable.wy_icon_male:R.drawable.wy_icon_female);//加入图片
                    map.put("lv_tv_name", poi.getString("name"));
                    map.put("lv_tv_info", poi.getString("address"));
                    usermapList.add(map);

                    //users(only has userId&name&gender)
                    User user = new User();
                    user.setUserId(poi.getString("userId"));
                    user.setName(poi.getString("name"));
                    user.setGender(poi.getString("gender"));
                    userList.add(user);

                    //save userId-name[k-v value] cache
//                    ACache.get(NearByActivity.this).put(user.getUserId(),user.getName());
                    //save userId-json[k-v value] cache
                    ACache.get(NearByActivity.this).put(user.getUserId(),JSON.toJSONString(user));

                    //save result lists into ram
                    AppContext.getInstance().setUsermapList(usermapList);
                    AppContext.getInstance().setUserList(userList);
                }
                //设置适配器
                setListViewAdapter();
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
