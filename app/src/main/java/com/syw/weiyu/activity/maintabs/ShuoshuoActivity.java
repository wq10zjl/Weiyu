package com.syw.weiyu.activity.maintabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

//import com.qq.e.ads.AdListener;
//import com.qq.e.ads.AdRequest;
//import com.qq.e.ads.AdSize;
//import com.qq.e.ads.AdView;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.LBS.LBSCloud;
import com.syw.weiyu.R;
import com.syw.weiyu.ad.MoGo;
import com.syw.weiyu.adapter.ShuoshuoAdapter;

import net.tsz.afinal.http.AjaxCallBack;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.rong.imkit.RongIM;

/**
 * Created by songyouwei on 2015/2/9.
 */
public class ShuoshuoActivity extends FragmentActivity {

    //用于视图适配器的mapList
    List<HashMap<String, String>> shuoshuomapList = new ArrayList<>();

    ListView listView;
    ShuoshuoAdapter adapter;

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

    //AdMoGo
    MoGo moGo;

    //LBS callback
    AjaxCallBack<String> lbsCloudSearchCallback = new LBSCloudSearchCallback();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_shuoshuo);
        initViews();

//        initHeader();
        initUltraPullToRefresh();
        initListView();

//        initBaiduBannerAd();
        moGo = new MoGo(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Weiyu","ShoshuoActivity onResume");

        //读取缓存的lists
        shuoshuomapList = AppContext.getInstance().getShuoshuomapList();
        //若空，则刷新数据（延迟一秒），否者直接设置适配器
        if(shuoshuomapList.isEmpty()) {
            new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    mPtrFrame.autoRefresh();
                }
            }.sendEmptyMessageDelayed(1,500);
        } else {
            setListViewAdapter();
        }

        //MoGoBanner
        moGo.showBannerAd();
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

    @Override
    protected void onDestroy() {
        moGo.onBannerDestory();
        super.onDestroy();
    }

    private void initViews() {
        ImageView btnAdd = (ImageView) findViewById(R.id.header_iv_right);
        btnAdd.setImageResource(R.drawable.wy_icon_add);
        btnAdd.setVisibility(View.VISIBLE);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = getAddShuoshuoAlertDialog();
                dialog.show();
            }
        });

        TextView tvTitle = (TextView) findViewById(R.id.header_tv_title);
        tvTitle.setText("首页");
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
                LBSCloud.getInstance().nearbyShuoshuoSearch(lbsCloudSearchCallback);
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
        listView = (ListView) findViewById(R.id.lv_shuoshuo);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIM.getInstance().startPrivateChat(ShuoshuoActivity.this, shuoshuomapList.get(position).get("userId").toString(), shuoshuomapList.get(position).get("shuoshuo_tv_name").toString());
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
        adapter = new ShuoshuoAdapter(this,shuoshuomapList);
//        adapter = new SimpleAdapter(
//                ShuoshuoActivity.this,
//                shuoshuomapList,
//                R.layout.wy_shuoshuo_lv_item,
//                new String[]{"shuoshuo_tv_name","shuoshuo_tv_address", "shuoshuo_tv_time", "shuoshuo_tv_content"},
//                new int[]{ R.id.shuoshuo_tv_name,R.id.shuoshuo_tv_address, R.id.shuoshuo_tv_time, R.id.shuoshuo_tv_content});
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
            Log.d("Weiyu", " LBSCloud poi search nearby shuoshuo return:" + s);
            //结束下拉刷新
            mPtrFrame.refreshComplete();
            //解析数据并存入
            JSONObject result = JSON.parseObject(s);
            if (result.getString("status").equals("0")) {
                //clear this list
                shuoshuomapList.clear();

                JSONArray poiArray = result.getJSONArray("contents");
                for (int i=0; i<poiArray.size(); i++) {
                    JSONObject poi = poiArray.getJSONObject(i);

                    //map for adapter
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userId", poi.getString("userId"));
                    map.put("shuoshuo_tv_name", poi.getString("userName"));
                    map.put("shuoshuo_tv_address", poi.getString("province")+poi.getString("district"));
                    map.put("shuoshuo_tv_time", new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH).format(new Timestamp(Long.parseLong(poi.getString("create_time"))*1000)));
                    map.put("shuoshuo_tv_content", poi.getString("content"));
                    shuoshuomapList.add(map);

                    //save result lists into ram
                    AppContext.getInstance().setShuoshuomapList(shuoshuomapList);
                }
                //设置适配器
                setListViewAdapter();
            }
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            Log.d("Weiyu", " LBSCloud poi search nearby shuoshuo failure:" + strMsg);
            //结束下拉刷新
            mPtrFrame.refreshComplete();
        }
    }

    private AlertDialog getAddShuoshuoAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ShuoshuoActivity.this,AlertDialog.THEME_HOLO_LIGHT);
        // Get the layout inflater
        final LayoutInflater inflater = ShuoshuoActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.wy_dialog_addshuoshuo, null);
        final EditText contentET = (EditText)view.findViewById(R.id.et_shuoshuo_content);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        LBSCloud.getInstance().publishShuoshuo(contentET.getText().toString(), new AjaxCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                if (JSON.parseObject(s).getString("status").equals("0")) {
                                    Toast.makeText(ShuoshuoActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ShuoshuoActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                Toast.makeText(ShuoshuoActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });

                        new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                mPtrFrame.autoRefresh();
                            }
                        }.sendEmptyMessageDelayed(1, 1000);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        builder.create().dismiss();
                    }
                });
        return builder.create();
    }
}