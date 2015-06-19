package com.syw.weiyu.core;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.adp.WeiyuBannerCustomEventPlatformAdapter;
import com.syw.weiyu.adp.WeiyuCustomEventPlatformEnum;
import com.syw.weiyu.av.WeiyuLayout;
import com.syw.weiyu.bean.*;
import com.syw.weiyu.controller.listener.WeiyuListener;
import com.syw.weiyu.dao.im.RongCloud;
import com.syw.weiyu.dao.img.BmobImageDao;
import com.syw.weiyu.dao.location.LocationDao;
import com.syw.weiyu.dao.push.BmobPushHelper;
import com.syw.weiyu.dao.shuoshuo.*;
import com.syw.weiyu.dao.user.LocalAccountDao;
import com.syw.weiyu.dao.user.UserDao;
import com.syw.weiyu.dao.user.UserDao4Bmob;
import com.syw.weiyu.splash.WeiyuSplash;
import com.syw.weiyu.splash.WeiyuSplashListener;
import com.syw.weiyu.util.StringUtil;
import com.syw.weiyu.util.WeiyuSize;
import com.syw.weiyu.util.WeiyuSplashMode;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.InformationNotificationMessage;

import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-26
 * desc: Api
 */
public class WeiyuApi {

    private LocationDao locationDao;
    private LocalAccountDao localAccountDao;
    private UserDao userDao;
    private CommentDao commentDao;
    private ShuoshuoDao shuoshuoDao;
    private WeiyuApi(){
        locationDao = new LocationDao();
        localAccountDao = new LocalAccountDao();
        userDao = new UserDao4Bmob();
        commentDao = new CommentDao4Bomb();
        shuoshuoDao = new ShuoshuoDao4Bmob();
    }
    private static WeiyuApi api;
    public static WeiyuApi get() {
        if (api == null) api = new WeiyuApi();
        return api;
    }

    /**
     * =============================================================
     * ------------------------账户部分---------------------------
     * =============================================================
     */

    /**
     * 获取当前账户
     * @return
     * @throws AppException 暂无账户
     */
    public Account getAccount() throws AppException {
        return localAccountDao.get();
    }

    /**
     * 登录接口
     * 连接IM服务器
     * 更新最后在线时间&地理位置信息
     * @param token
     * @throws AppException 无token
     */
    public void login(String token) throws AppException {
        RongCloud.connect(token);
        try {
            userDao.update(localAccountDao.get().getBmobObjectId(), null, null, null, locationDao.get(), null);
        } catch (AppException e) {
            Logger.e(e.getMessage());
        }
    }

    /**
     * 登出
     * 1.更新最后在线时间
     * 2.登出IM服务器
     * 3.退出App
     */
    public void logout() {
        updateLastOnlineTimestamp();
        RongCloud.disconnect();
        System.exit(0);
    }


    /**
     * =============================================================
     * ------------------------用户部分---------------------------
     * =============================================================
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
        //拿用户信息
        userDao.getUserWithoutCache(id, new Listener<User>() {
            @Override
            public void onSuccess(final User user) {
                //老用户，直接修改资料
                userDao.update(user.getObjectId(), id, name, gender, locationDao.get(), new Listener<Null>() {
                    @Override
                    public void onSuccess(Null data) {
                        getTokenAndInitAccount(user.getObjectId(), id, name, gender, listener);
                    }

                    @Override
                    public void onFailure(String msg) {
                        listener.onFailure(msg);
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                //新用户，创建用户资料
                userDao.create(id, name, gender, locationDao.get(), new Listener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        getTokenAndInitAccount(data, id, name, gender, listener);
                    }

                    @Override
                    public void onFailure(String msg) {
                        listener.onFailure(msg);
                    }
                });
            }
        });
    }

    /**
     * 设置本地账户
     * 包括基础资料，token，bmobObjectId
     * @param bmobObjectId
     * @param id
     * @param name
     * @param gender
     * @param listener
     */
    private void getTokenAndInitAccount(final String bmobObjectId, final String id, final String name, final String gender, final Listener<String> listener) {
        //拿token
        RongCloud.getToken(id, name, gender.equals("男") ? AppConstants.url_user_icon_male : AppConstants.url_user_icon_female, new Listener<String>() {
            @Override
            public void onSuccess(String data) {
                Account account = new Account(id, name, gender, data);
                account.setBmobObjectId(bmobObjectId);
                localAccountDao.set(account);
                if (listener != null) listener.onSuccess(data);

                //补充的初始化操作
                App.getCtx().doThingsWithAccount(account);
            }

            @Override
            public void onFailure(String msg) {
                if (listener != null) listener.onFailure(msg);
            }
        });
    }

    /**
     * 修改资料
     * @param name
     * @param gender
     * @param listener
     */
    public void updateProfile(final String id, final String name,final String gender,final Listener<Null> listener) {
        try {
            final Account account = localAccountDao.get();
            userDao.update(account.getBmobObjectId(), id, name, gender, locationDao.get(), new Listener<Null>() {
                @Override
                public void onSuccess(Null data) {
                    //set local
                    account.setName(name);
                    account.setGender(gender);
                    localAccountDao.set(account);
                    //再刷新RongCloud用户信息
                    RongCloud.refreshUserInfo(id, name, gender.equals("男") ? AppConstants.url_user_icon_male : AppConstants.url_user_icon_female, listener);
                }

                @Override
                public void onFailure(String msg) {
                    listener.onFailure(msg);
                }
            });
        } catch (AppException e) {
            listener.onFailure(e.getMessage());
        }
    }

    /**
     * 查看附近的人
     * @param pageIndex
     * @param listener
     */
    public void getNearbyUsers(int pageIndex,Listener<UserList> listener) {
        MLocation location = locationDao.get();
        int pageSize = AppConstants.page_size_default;
        userDao.getNearbyUsers(location, pageSize, pageIndex, listener);
        updateLastOnlineTimestamp();
    }

    /**
     * 获取用户资料
     * @param id
     * @return
     */
    public User getUser(String id) {
        return userDao.getUser(id);
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
     * 更新最后在线时间
     */
    public void updateLastOnlineTimestamp() {
        try {
            userDao.updateLastOnlineTimestamp(localAccountDao.get().getBmobObjectId(), System.currentTimeMillis());
        } catch (AppException e) {
            e.getMessage();
        }
    }


    /**
     * say hi
     */
    public void sayHi(final String targetUserId, final Listener<Null> listener) {
        try {
            final Account account = localAccountDao.get();
            RongIMClient.getInstance().sendMessage(
                    Conversation.ConversationType.PRIVATE,
                    targetUserId,
                    new InformationNotificationMessage(account.getName()+"向你打了个招呼"),
                    null,
                    new RongIMClient.SendMessageCallback() {
                @Override
                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                    listener.onFailure(errorCode.getMessage());
                }

                @Override
                public void onSuccess(Integer integer) {
                    listener.onSuccess(null);
                    RongIMClient.getInstance().insertMessage(
                            Conversation.ConversationType.PRIVATE,
                            targetUserId,
                            account.getId(),
                            new InformationNotificationMessage("你向对方打了个招呼"),
                            null
                    );
                }
            });
        } catch (AppException e) {
            e.printStackTrace();
        }
    }

    /**
     * =============================================================
     * ------------------------位置部分---------------------------
     * =============================================================
     */

    /**
     * 定位，保存位置数据
     */
    public void locate() {
        locationDao.set();
    }

    /**
     * 获取缓存的位置数据
     * @return
     */
    public MLocation getSavedLocation() {
        return locationDao.get();
    }


    /**
     * =============================================================
     * ------------------------说说部分---------------------------
     * =============================================================
     */

    /**
     * 获取缓存的说说列表
     * @return
     * @throws AppException 无缓存数据
     */
    public ShuoshuoList getCachedNearbyShuoshuo() throws AppException {
        ShuoshuoList list = (ShuoshuoList) App.getCache(App.KEY_NEARBYSHUOSHUOS);
        if (list!=null && list.getShuoshuos()!=null && list.getShuoshuos().size()>0) return list;
        else throw new AppException("无缓存数据");
    }

    /**
     * 获取附近的说说
     * @param pageIndex
     * @return
     */
    public void getNearbyShuoshuo(int pageIndex,Listener<ShuoshuoList> listener) {
        shuoshuoDao.getNearbyShuoshuos(locationDao.get(),AppConstants.page_size_default,pageIndex,listener);
        updateLastOnlineTimestamp();
    }

    /**
     * 获取某个用户的说说
     * @param userId
     * @param pageIndex
     * @param listener
     */
    public void getUserShuoshuo(String userId,int pageIndex,Listener<ShuoshuoList> listener) {
        shuoshuoDao.getUserShuoshuos(userId, AppConstants.page_size_default, pageIndex, listener);
    }

    /**
     * 获取某一个说说
     */
    public void getShuoshuo(long id, Listener<Shuoshuo> listener) {
        shuoshuoDao.getShuoshuo(id, listener);
    }
    
    /**
     * 发布说说
     * @param content
     * @param listener
     */
    public void publishShuoshuo(String content,Listener<Null> listener) throws AppException {
        if (StringUtil.isEmpty(content)) throw new AppException("什么都没写←_←");
        Account account = localAccountDao.get();
        shuoshuoDao.create(account, locationDao.get(), content, System.currentTimeMillis(), listener);
    }

    /**
     * 发布带图片的说说
     */
    public void publishShuoshuoWithPic(final String content,String picPath, final Listener<Null> listener) throws AppException {
        if (StringUtil.isEmpty(content)) throw new AppException("什么都没写←_←");
        final Account account = localAccountDao.get();
        BmobImageDao.getInstance(App.getCtx()).saveThumbnailPic(picPath, new Listener<String>() {
            @Override
            public void onSuccess(String data) {
                shuoshuoDao.create(account, locationDao.get(), content, data, System.currentTimeMillis(), listener);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * “❤”
     * @param shuoshuo
     */
    public void addLiked(Shuoshuo shuoshuo) {
        shuoshuoDao.addLikedCount(shuoshuo);
    }

    /**
     * 获取说说的评论
     * @param ssId 说说ID
     * @param listener
     * @throws AppException
     */
    public void getShuoshuoComments(final long ssId, final Listener<List<Comment>> listener) {
        commentDao.getComments(ssId, listener);
    }

    /**
     * 添加评论
     * @param shuoshuo
     * @param content
     * @param atUserId
     * @param listener
     */
    public void addComment(final Shuoshuo shuoshuo, String content, final String atUserId, final Listener<Comment> listener) {
        if (StringUtil.isEmpty(content)) {
            listener.onFailure("什么都没写←_←");
            return;
        }
        commentDao.addComment(shuoshuo.getId(), content, new Listener<Comment>() {
            @Override
            public void onSuccess(Comment data) {
                listener.onSuccess(data);
                //++
                shuoshuoDao.addCommentCount(shuoshuo);
                //push
                data.setShuoshuo(shuoshuo.getContent());
                BmobPushHelper.pushCommentMessage(shuoshuo.getUserId(), data);
                if (!StringUtil.isEmpty(atUserId))BmobPushHelper.pushCommentMessage(atUserId, data);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * =============================================================
     * ------------------------界面部分---------------------------
     * =============================================================
     */

    /**
     * 设置底部聊天未读显示
     * @param unreadIndicator
     */
    public void setBottomChatTabUnreadIndicator(final View unreadIndicator) {
        int i = RongIMClient.getInstance().getTotalUnreadCount();
        if (i == 0) unreadIndicator.setVisibility(View.INVISIBLE);
        else unreadIndicator.setVisibility(View.VISIBLE);
    }


    /**
     * =============================================================
     * ------------------------广告部分---------------------------
     * =============================================================
     */

    //adsmogo
    private WeiyuLayout weiyuLayoutCode;
    /**
     * 在Activity的onDestroy方法下调用
     */
    public void onBannerDestory() {
        WeiyuLayout.clear();
        if (weiyuLayoutCode!=null) weiyuLayoutCode.clearThread();
    }

    public View getBannerAdView(Activity activity,final AdListener listener) {
        /**
         * 初始化adsMogoView
         * 参数：第一个activity,第二个mogoID（该值为芒果后台申请的生产的芒果ID，非单一平台ID）,第三个设置广告展示位置,第四个请求广告尺寸,
         * 第五个是否手动刷新true：是手动刷新（芒果后台轮换时间必须为禁用才会生效）	，false:自动轮换
         */
        weiyuLayoutCode = new WeiyuLayout(activity, AppConstants.adsmogo_appid, WeiyuSize.WeiyuAutomaticScreen);

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
                listener.onFailedReceiveAd();
            }

            @Override
            public void onClickAd(String s) {
            }

            @Override
            public boolean onCloseAd() {
                if (listener!=null)listener.onCloseAd();
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

    public void showSplashAd(Activity activity,final AdListener listener) {
        WeiyuSplash weiyuSplash = new WeiyuSplash(activity,AppConstants.adsmogo_appid, WeiyuSplashMode.FULLSCREEN);
        //设置开屏广告监听
        weiyuSplash.setWeiyuSplashListener(new WeiyuSplashListener() {
            @Override
            public void onSplashClickAd(String s) {

            }

            @Override
            public void onSplashRealClickAd(String s) {

            }

            @Override
            public void onSplashError(String s) {
                listener.onFailedReceiveAd();
            }

            @Override
            public void onSplashSucceed() {

            }

            @Override
            public void onSplashClose() {
                listener.onCloseAd();
            }
        });
        weiyuSplash.setCloseButtonVisibility(View.VISIBLE);
    }
}
