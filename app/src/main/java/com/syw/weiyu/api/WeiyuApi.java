package com.syw.weiyu.api;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
import com.syw.weiyu.R;
import com.syw.weiyu.adp.WeiyuBannerCustomEventPlatformAdapter;
import com.syw.weiyu.adp.WeiyuCustomEventPlatformEnum;
import com.syw.weiyu.av.WeiyuLayout;
import com.syw.weiyu.bean.*;
import com.syw.weiyu.controller.listener.WeiyuListener;
import com.syw.weiyu.dao.im.RongCloud;
import com.syw.weiyu.dao.im.TokenDao;
import com.syw.weiyu.dao.location.LocationDao;
import com.syw.weiyu.dao.location.UserPoiDao;
import com.syw.weiyu.dao.shuoshuo.ShuoshuoDao;
import com.syw.weiyu.dao.user.AccountDao;
import com.syw.weiyu.dao.user.UserDao;
import com.syw.weiyu.splash.WeiyuSplash;
import com.syw.weiyu.splash.WeiyuSplashListener;
import com.syw.weiyu.util.StringUtil;
import com.syw.weiyu.util.WeiyuSize;
import com.syw.weiyu.util.WeiyuSplashMode;

import java.util.List;

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
     * 3.设置账户信息到本地
     * @param id
     * @param name
     * @param gender
     * @param listener 包含返回token
     */
    public void register(final String id,final String name,final String gender,final Listener<String> listener) {
        new UserPoiDao().create(new User(id, name, gender), new LocationDao().get(), new Listener<String>() {
            @Override
            public void onCallback(@NonNull CallbackType callbackType, @Nullable String data, @Nullable String msg) {
                //poi创建成功
                if (callbackType == CallbackType.onSuccess) {
                    //拿token
                    new TokenDao().get(id, name, null, new Listener<String>() {
                        @Override
                        public void onCallback(@NonNull CallbackType callbackType, @Nullable String data, @Nullable String msg) {
                            if (callbackType == CallbackType.onSuccess) {
                                Account account = new Account(id, name, gender, data);
                                new AccountDao().set(account);
                                listener.onCallback(CallbackType.onSuccess, data, null);
                            } else {
                                listener.onCallback(CallbackType.onFailure, null, msg);
                            }
                        }
                    });
                } else {
                    listener.onCallback(CallbackType.onFailure, null, msg);
                }
            }
        });
    }

    public Account getAccount() throws AppException {
        return new AccountDao().get();
    }

    /**
     * 登录接口
     * 连接IM服务器
     * @param token
     * @throws AppException 无token
     */
    public void login(String token) throws AppException {
        RongCloud.connect(token);
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
    public User getUser(String id) throws AppException {
        return new UserDao().getUser(id);
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

    public MLocation getSavedLocation() {
        return new LocationDao().get();
    }


    /**
     * =========================================
     * ----------------说说部分------------------
     * =========================================
     */

    /**
     * 刷新（获取第一页的说说）
     * @return
     * @throws AppException 无缓存数据
     */
    public ShuoshuoList getCachedNearbyShuoshuo() throws AppException {
        ShuoshuoList list = (ShuoshuoList) AppContext.get(AppContext.KEY_NEARBYSHUOSHUOS);
        if (list!=null && list.getShuoshuos()!=null && list.getShuoshuos().size()>0) return list;
        else throw new AppException("无缓存数据");
    }

    /**
     * 获取附近的说说
     * @param pageIndex
     * @return
     */
    public void getNearbyShuoshuo(int pageIndex,Listener<ShuoshuoList> listener) {
        new ShuoshuoDao().getNearByList(pageIndex, listener);
    }

    /**
     * 获取说说详情
     * @param shuoshuo
     * @return
     * @throws AppException
     */
    public void getShuoshuoDetail(final Shuoshuo shuoshuo, final Listener<Shuoshuo> listener) throws AppException {
        new ShuoshuoDao().getComments(shuoshuo.getId(), new Listener<List<Comment>>() {
            @Override
            public void onCallback(@NonNull CallbackType callbackType, @Nullable List<Comment> data, @Nullable String msg) {
                if (callbackType == CallbackType.onSuccess) {
                    shuoshuo.setComments(data);
                    listener.onCallback(CallbackType.onSuccess,shuoshuo,msg);
                } else {
                    listener.onCallback(CallbackType.onFailure,null,msg);
                }
            }
        });
    }

    public void publishShuoshuo(String content,Listener<String> listener) {
        new ShuoshuoDao().add(content,listener);
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

    public View getBannerAdView(Activity activity,final Listener<String> listener) {
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
                listener.onCallback(Listener.CallbackType.onAdError,null,"onFailedReceive");
            }

            @Override
            public void onClickAd(String s) {
                listener.onCallback(Listener.CallbackType.onAdClick,null,s);
            }

            @Override
            public boolean onCloseAd() {
                listener.onCallback(Listener.CallbackType.onAdClose,null,null);
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

    public void showSplashAd(Activity activity,final Listener<String> listener) {
        WeiyuSplash weiyuSplash = new WeiyuSplash(activity,activity.getString(R.string.adsmogo_appid), WeiyuSplashMode.FULLSCREEN);
        //设置开屏广告监听
        weiyuSplash.setWeiyuSplashListener(new WeiyuSplashListener() {
            @Override
            public void onSplashClickAd(String s) {
                listener.onCallback(Listener.CallbackType.onAdClick,null,s);
            }

            @Override
            public void onSplashRealClickAd(String s) {

            }

            @Override
            public void onSplashError(String s) {
                listener.onCallback(Listener.CallbackType.onAdError,null,s);
            }

            @Override
            public void onSplashSucceed() {

            }

            @Override
            public void onSplashClose() {
                listener.onCallback(Listener.CallbackType.onAdClose,null,null);
            }
        });
        weiyuSplash.setCloseButtonVisibility(View.VISIBLE);
    }
}
