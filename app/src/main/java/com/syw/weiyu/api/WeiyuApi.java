package com.syw.weiyu.api;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import com.syw.weiyu.AppException;
import com.syw.weiyu.R;
import com.syw.weiyu.adp.WeiyuBannerCustomEventPlatformAdapter;
import com.syw.weiyu.adp.WeiyuCustomEventPlatformEnum;
import com.syw.weiyu.av.WeiyuLayout;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.controller.listener.WeiyuListener;
import com.syw.weiyu.dao.im.RongCloud;
import com.syw.weiyu.dao.im.TokenDao;
import com.syw.weiyu.dao.location.LocationDao;
import com.syw.weiyu.dao.location.UserPoiDao;
import com.syw.weiyu.dao.shuoshuo.ShuoshuoDao;
import com.syw.weiyu.dao.user.AccountDao;
import com.syw.weiyu.splash.WeiyuSplash;
import com.syw.weiyu.splash.WeiyuSplashListener;
import com.syw.weiyu.util.WeiyuSize;
import com.syw.weiyu.util.WeiyuSplashMode;

/**
 * author: songyouwei
 * date: 2015-05-26
 * desc: Api
 */
public class WeiyuApi {

    private WeiyuApi(){}
    private static WeiyuApi api;
    public static WeiyuApi get() {
        if (api == null) api = new WeiyuApi();
        return api;
    }

    /**
     * =========================================
     * ----------------用户部分------------------
     * =========================================
     */

    /**
     * 注册接口
     * 1.使用账户信息在LBS云创建POI节点
     * 2.获取token
     * 3.设置账户
     * @param id
     * @param name
     * @param gender
     * @throws AppException
     */
    public void register(String id,String name,String gender) throws AppException {
        new UserPoiDao().create(new User(id, name, gender), new LocationDao().get());
        String token = new TokenDao().get(id, name, null);
        Account account = new Account(id,name,gender,token,new LocationDao().get());
        new AccountDao().set(account);
    }

    /**
     * 登录接口
     * 1.连接IM服务器
     * @return
     */
    public void login() throws AppException {
        RongCloud.connect(new AccountDao().get().getToken());
    }

    /**
     * 登出
     * 1.更新最后在线时间
     * 2.登出IM服务器
     * @param id
     */
    public void logout(String id) {

    }

    /**
     * 是否在线
     * @param id
     * @return
     */
    public boolean isOnline(String id) {
        return false;
    }

    /**
     * 获取最后在线时间
     * @param id
     * @return
     */
    public int getLastOnlineTimestamp(String id) {
        return 0;
    }

    /**
     * 获取用户资料
     * @param id
     * @return
     */
    public User getUserProfile(String id) {
        return null;
    }

    /**
     * 更新用户资料
     */
    public void updateUserProfile(User user) {

    }

    /**
     * =========================================
     * ----------------位置部分------------------
     * =========================================
     */

    /**
     * 定位，保存位置数据
     */
    public void locate() {
        new LocationDao().set();
    }

    public void updateUserLocation(Listener listener) {

    }

    public void getCachedLocation(Listener listener) {

    }


    /**
     * =========================================
     * ----------------说说部分------------------
     * =========================================
     */

    /**
     * 刷新（获取第一页的说说）
     * @return
     * @throws AppException
     */
    public ShuoshuoList refreshNearbyShuoshuo() throws AppException {
        return getNearbyShuoshuo(0);
    }

    /**
     * 获取附近的说说
     * 优先从内存缓存中获取
     * @param pageIndex
     * @return
     * @throws AppException
     */
    public ShuoshuoList getNearbyShuoshuo(int pageIndex) throws AppException {
        return null;
    }

    /**
     * 获取说说详情
     * @param shuoshuo
     * @return
     * @throws AppException
     */
    public Shuoshuo getShuoshuoDetail(Shuoshuo shuoshuo) throws AppException {
        shuoshuo.setCommentList(new ShuoshuoDao().getComments(shuoshuo.getId()));
        return shuoshuo;
    }

    public void publishShuoshuo(String content) throws AppException {
        new ShuoshuoDao().add(content);
    }

    /**
     * =========================================
     * ----------------广告部分------------------
     * =========================================
     */

    //adsmogo
    WeiyuLayout weiyuLayoutCode;

    /**
     * 在Activity的onDestroy方法下调用
     */
    public void onBannerDestory() {
        WeiyuLayout.clear();
        weiyuLayoutCode.clearThread();
    }

    Listener listener;
    public void init(Listener listener) {
        this.listener = listener;
    }

    public View getBannerAdView(Activity activity,final Listener listener) {
        /**
         * 初始化adsMogoView
         * 参数：第一个activity,第二个mogoID（该值为芒果后台申请的生产的芒果ID，非单一平台ID）,第三个设置广告展示位置,第四个请求广告尺寸,
         * 第五个是否手动刷新true：是手动刷新（芒果后台轮换时间必须为禁用才会生效）	，false:自动轮换
         */
        weiyuLayoutCode = new WeiyuLayout(activity, activity.getString(R.string.adsmogo_appid), WeiyuSize.WeiyuAutomaticScreen);

        weiyuLayoutCode.setWeiyuListener(new WeiyuListener() {
            @Override
            public void onInitFinish() {

            }

            @Override
            public void onRequestAd(String s) {

            }

            @Override
            public void onRealClickAd() {

            }

            @Override
            public void onReceiveAd(ViewGroup viewGroup, String s) {

            }

            @Override
            public void onFailedReceiveAd() {
                listener.onCallback(Listener.CallbackType.onAdError,"onFailedReceive");
            }

            @Override
            public void onClickAd(String s) {
                listener.onCallback(Listener.CallbackType.onAdClick,s);
            }

            @Override
            public boolean onCloseAd() {
                listener.onCallback(Listener.CallbackType.onAdClose,"");
                return false;
            }

            @Override
            public void onCloseMogoDialog() {

            }

            @Override
            public Class<? extends WeiyuBannerCustomEventPlatformAdapter> getCustomEvemtPlatformAdapterClass(WeiyuCustomEventPlatformEnum weiyuCustomEventPlatformEnum) {
                return null;
            }
        });
        weiyuLayoutCode.setCloseButtonVisibility(View.VISIBLE);
        weiyuLayoutCode.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        weiyuLayoutCode.downloadIsShowDialog = true;

        return weiyuLayoutCode;
    }

    public void showSplashAd(Activity activity,final Listener listener) {
        WeiyuSplash weiyuSplash = new WeiyuSplash(activity,activity.getString(R.string.adsmogo_appid), WeiyuSplashMode.FULLSCREEN);
        //设置开屏广告监听
        weiyuSplash.setWeiyuSplashListener(new WeiyuSplashListener() {
            @Override
            public void onSplashClickAd(String s) {
                listener.onCallback(Listener.CallbackType.onAdClick,s);
            }

            @Override
            public void onSplashRealClickAd(String s) {

            }

            @Override
            public void onSplashError(String s) {
                listener.onCallback(Listener.CallbackType.onAdError,s);
            }

            @Override
            public void onSplashSucceed() {

            }

            @Override
            public void onSplashClose() {
                listener.onCallback(Listener.CallbackType.onAdClose,"");
            }
        });
        weiyuSplash.setCloseButtonVisibility(View.VISIBLE);
    }
}
