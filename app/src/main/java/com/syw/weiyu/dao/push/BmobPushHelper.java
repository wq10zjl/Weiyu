package com.syw.weiyu.dao.push;

import android.content.Context;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.core.*;
import com.syw.weiyu.dao.user.LocalAccountDao;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * author: songyouwei
 * date: 2015-06-10
 * desc:
 */
/**
 * author: songyouwei
 * date: 2015-06-10
 * desc:
 */
public class BmobPushHelper {

    public static void pushCommentMessage(String toUserId, Comment comment) {
        try {
            JSONObject jsonObject = new JSONObject(JSON.toJSONString(comment));
            pushJsonObject(toUserId, jsonObject);
        } catch (JSONException e) {
            Logger.e(e.getMessage());
        }
    }

    public static void pushNotification(String userId, String message) {
        BmobPushManager bmobPush = new BmobPushManager(App.getCtx());
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("userId", userId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage(message);
    }

    private static void pushStringMessage(String userId, String message) {
        BmobPushManager bmobPush = new BmobPushManager(App.getCtx());
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("userId", userId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage(message);
    }

    private static void pushJsonObject(String userId, JSONObject jsonObject) {
        BmobPushManager bmobPush = new BmobPushManager(App.getCtx());
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("userId", userId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage(jsonObject);
    }


    /**
     * 初始化bmobpush相关数据，成功后开启推送
     * @param context
     * @param account
     */
    public static void initAndStartPushClient(final Context context, final Account account) {
        if (account.getHasInitedBmobPush() == 1) {
            BmobInstallation.getCurrentInstallation(context).save();
            //start push
            BmobPush.startWork(context, AppConstants.bmob_app_id);
            return;
        }
        MyBmobInstallation installation = new MyBmobInstallation(context);
        installation.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                updateInstallation(context, account.getId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        //start push
                        BmobPush.startWork(context, AppConstants.bmob_app_id);
                        //update account
                        account.setHasInitedBmobPush(1);
                        new LocalAccountDao().set(account);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                if (i == 132) {//installation ID already token.
                    //start push
                    BmobPush.startWork(context, AppConstants.bmob_app_id);
                    //update account
                    account.setHasInitedBmobPush(1);
                    new LocalAccountDao().set(account);
                }
            }
        });
    }

    /**
     * 更新userId
     * @param context
     * @param userId
     * @param updateListener
     */
    private static void updateInstallation(final Context context, final String userId, final UpdateListener updateListener) {
        BmobQuery<MyBmobInstallation> query = new BmobQuery<>();
        query.addWhereEqualTo("installationId", MyBmobInstallation.getInstallationId(context));
        query.findObjects(context, new FindListener<MyBmobInstallation>() {
            @Override
            public void onSuccess(List<MyBmobInstallation> list) {
                if (list.size() > 0) {
                    MyBmobInstallation installation = list.get(0);
                    installation.setUserId(userId);
                    installation.update(context, updateListener);
                } else updateListener.onFailure(0, "has no BmobInstallation");
            }

            @Override
            public void onError(int i, String s) {
                updateListener.onFailure(i, s);
            }
        });
    }


}