package com.syw.weiyu.dao.im;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.View;
import com.syw.weiyu.R;
import com.syw.weiyu.core.App;
import com.syw.weiyu.core.AppConstants;
import com.syw.weiyu.core.AppException;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.dao.location.LocationDao;
import com.syw.weiyu.ui.session.PhotoActivity;
import com.syw.weiyu.ui.user.UserHomeActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 6、会话界面操作的监听器：ConversationBehaviorListener。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 */
public final class RongCloudEvent implements
        RongIM.UserInfoProvider,
        RongIM.LocationProvider,
        RongIM.ConversationBehaviorListener
//        RongIM.OnReceiveUnreadCountChangedListener
{

    private static final String TAG = RongCloudEvent.class.getSimpleName();

    private static RongCloudEvent mRongCloudInstance;

    private Context mContext;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
//        RongIM.setGroupInfoProvider(this);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
    }

    /*
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {
//        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(this);////在api中设置了
//        RongIM.getInstance().setReceiveMessageListener(this);//设置消息接收监听器。
//        RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
//        RongIM.getInstance().setConnectionStatusListener(this);//设置连接状态监听器。
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * 如果在聊天中遇到的聊天对象是没有登录过的用户（即没有通过融云服务器鉴权过的），
     * RongIM 是不知道用户信息的，
     * RongIM 将调用此 Provider 获取用户信息。
     * @param userId
     * @return
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        User user = WeiyuApi.get().getUser(userId);
        Uri male = Uri.parse(AppConstants.url_user_icon_male);
        Uri female = Uri.parse(AppConstants.url_user_icon_female);
        Uri nogender = Uri.parse(AppConstants.url_user_icon_female);
//        Uri male = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.drawable.wy_icon_male);
//        Uri female = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.drawable.wy_icon_female);
//        Uri nogender = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.drawable.wy_icon_nogender);
        return user==null?
                new UserInfo(
                userId,
                "未知",
                nogender):
                new UserInfo(
                        user.getId(),
                        user.getName(),
                        user.getGender().equals("男")?male:female);
    }

    @Override
    public void onStartLocation(Context context, final LocationCallback locationCallback) {
        final MLocation location = new LocationDao().get();
        final double lat = Double.parseDouble(location.getLatitude());
        final double lng = Double.parseDouble(location.getLongitude());
        final StringBuffer uri = new StringBuffer("http://api.map.baidu.com/staticimage?width=400&height=200&&zoom=11&markers=").append(lng).append(",").append(lat).append("&center=").append(location.getCity());
        new Runnable(){
            @Override
            public void run() {
                locationCallback.onSuccess(LocationMessage.obtain(lat, lng,
                        location.getAddress(),
                        Uri.parse(uri.toString())));
            }
        }.run();
    }


    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        Intent intent = new Intent(context, UserHomeActivity.class);
        intent.putExtra("userId", userInfo.getUserId());
        context.startActivity(intent);
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if (message.getContent() instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) message.getContent();
            Intent intent = new Intent(context, PhotoActivity.class);

            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
            if (imageMessage.getThumUri() != null)
                intent.putExtra("thumbnail", imageMessage.getThumUri());

            context.startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
}