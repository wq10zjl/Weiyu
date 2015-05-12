package com.syw.weiyu.third.im;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.util.ACache;

import net.tsz.afinal.http.AjaxCallBack;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by zhjchen on 1/29/15.
 *
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
        RongIM.OnReceiveMessageListener,
        RongIM.OnSendMessageListener,
        RongIM.GetUserInfoProvider,
        RongIM.GetFriendsProvider,
        RongIM.GetGroupInfoProvider,
        RongIM.ConversationBehaviorListener,
        RongIM.ConnectionStatusListener,
        RongIM.LocationProvider {

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
     * 继而初始化默认的监听器
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
        RongIM.setGetUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGetFriendsProvider(this);//设置好友信息提供者.
        RongIM.setGetGroupInfoProvider(this);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
    }

    /*
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {
        RongIM.getInstance().setReceiveMessageListener(this);//设置消息接收监听器。
        RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
        RongIM.getInstance().setConnectionStatusListener(this);//设置连接状态监听器。
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
     * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
     *
     * @param message 接收到的消息的实体信息。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public void onReceived(RongIMClient.Message message, int left) {

        RongIMClient.MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.d("Weiyu",TAG+ "onReceived-TextMessage:" + textMessage.getContent());
            Log.d("Weiyu",TAG+ "onReceived-TextMessage:" + textMessage.getPushContent());

        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d("Weiyu",TAG+ "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d("Weiyu",TAG+ "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d("Weiyu",TAG+ "onReceived-RichContentMessage:" + richContentMessage.getContent());
        } else {
            Log.d("Weiyu",TAG+ "onReceived-其他消息，自己来判断处理");
        }

        /**
         * demo 代码 开发者需替换成自己的代码。
         */
//        Intent in = new Intent();
//        in.setAction(mContext.getString(R.string.RC_ACTION_RECEIVE_MESSAGE));
//        in.putExtra(mContext.getString(R.string.RC_UNREAD_COUNT), RongIM.getInstance().getTotalUnreadCount());
//        mContext.sendBroadcast(in);

    }


    /**
     * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message 消息。
     */
    @Override
    public RongIMClient.Message onSent(RongIMClient.Message message) {

        RongIMClient.MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.d("Weiyu",TAG+ "onSent-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d("Weiyu",TAG+ "onSent-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d("Weiyu",TAG+ "onSent-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d("Weiyu",TAG+ "onSent-RichContentMessage:" + richContentMessage.getContent());
        } else {
            Log.d("Weiyu",TAG+ "onSent-其他消息，自己来判断处理");
        }
        return  message;
    }

    /**
     * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
     *
     * @param userId 用户 Id。
     * @return 用户信息，（注：由开发者提供用户信息）。
     */
    @Override
    public RongIMClient.UserInfo getUserInfo(final String userId) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        //从ACache拿user
        final User user = JSON.parseObject(ACache.get(mContext).getAsString(userId), User.class);
        Log.d("Weiyu","cached user:"+user);
        //没有，发起网络请求并储存
        if (user == null) {
            LBSCloud.getInstance().getDetail(userId,new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONObject poi = JSON.parseObject(s).getJSONObject("poi");
                    //save to cache
                    ACache.get(mContext).put(userId, poi.toJSONString());
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                }
            });
            return new RongIMClient.UserInfo(userId,"未知",null);
        }
        return new RongIMClient.UserInfo(
                userId,
                user.getName()!=null?user.getName():"未知",
                user.getGender().equals("男")?"http://com-syw-weiyu.qiniudn.com/wy_icon_male.jpg":"http://com-syw-weiyu.qiniudn.com/wy_icon_female.jpg");
    }

    /**
     * 好友列表的提供者：GetFriendsProvider 的回调方法，获取好友信息列表。
     *
     * @return 获取好友信息列表，（注：由开发者提供好友列表信息）。
     */
    @Override
    public List<RongIMClient.UserInfo> getFriends() {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
//        return DemoContext.getInstance().getUserInfos();
        return null;
    }

    /**
     * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
     *
     * @param groupId 群组 Id.
     * @return 群组信息，（注：由开发者提供群组信息）。
     */
    @Override
    public RongIMClient.Group getGroupInfo(String groupId) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
//        return DemoContext.getInstance().getGroupMap().get(groupId);
        return null;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
     *
     * @param context          应用当前上下文。
     * @param conversationType 会话类型。
     * @param user             被点击的用户的信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onClickUserPortrait(Context context, RongIMClient.ConversationType conversationType, RongIMClient.UserInfo user) {
        Log.d("Weiyu",TAG+ "onClickUserPortrait");

        /**
         * demo 代码  开发者需替换成自己的代码。
         */
//        Log.d("Begavior", conversationType.getName() + ":" + user.getName());
//        Intent in = new Intent(context, UserInfoActivity.class);
//        in.putExtra("user_name", user.getName());
//        in.putExtra("user_id", user.getId());
//        context.startActivity(in);

        return false;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onClickMessage(Context context, RongIMClient.Message message) {
        Log.d("Weiyu",TAG+ "onClickMessage");

        /**
         * demo 代码  开发者需替换成自己的代码。
         */
//        if (message.getContent() instanceof LocationMessage) {
//            Intent intent = new Intent(context, LocationActivity.class);
//            intent.putExtra("location", message.getContent());
//            context.startActivity(intent);
//        }else  if(message.getContent() instanceof RichContentMessage){
//            RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
//            Log.d("Begavior",  "extra:"+mRichContentMessage.getExtra());
//
//        }
//
//        Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId());

        return false;
    }

    /**
     * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
     *
     * @param status 网络状态。
     */
    @Override
    public void onChanged(ConnectionStatus status) {
        if (status.equals(ConnectionStatus.DISCONNECTED) || status.equals(ConnectionStatus.UNKNOWN)) {
            //连接融云IM
            RongCloud.getInstance(mContext).connectRongCloud();
        }
        Log.d("Weiyu",TAG+ "onChanged:" + status);
    }


    /**
     * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
     *
     * @param context 上下文
     * @param callback 回调
     */
    @Override
    public void onStartLocation(Context context, final LocationCallback callback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
//        DemoContext.getInstance().setLastLocationCallback(callback);
//        context.startActivity(new Intent(context, LocationActivity.class));//SOSO地图
        MLocation location = AppContext.getInstance().getLocation();
        final double lat = Double.parseDouble(location.getLatitude());
        final double lng = Double.parseDouble(location.getLongitude());
        final StringBuffer uri = new StringBuffer("http://api.map.baidu.com/staticimage?width=300&height=150&&zoom=11&markers=").append(lng).append(",").append(lat).append("&center=").append(AppContext.getInstance().getLocation().getCity());
        new Runnable(){
            @Override
            public void run() {
                callback.onSuccess(LocationMessage.obtain(lat, lng,
                        AppContext.getInstance().getLocation().getAddress(),
                        Uri.parse(uri.toString())));
            }
        }.run();
    }
}
