package com.syw.weiyu.dao.push;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import com.syw.weiyu.core.App;

/**
 * author: songyouwei
 * date: 2015-06-10
 * desc:
 */
public class BmobPush {
    private static BmobPushManager bmobPushManager;

    public static void pushCommentMessage(String toUserId, String message) {
        String installationId = toUserId;
        BmobPushManager bmobPush = new BmobPushManager(App.getCtx());
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("installationId", installationId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage(message);
    }
}
