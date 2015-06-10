package com.syw.weiyu.dao.push;

import android.content.Context;
import cn.bmob.v3.BmobInstallation;

/**
 * author: youwei
 * date: 2015-06-10
 * desc:
 */
public class MyBmobInstallation extends BmobInstallation {
    public MyBmobInstallation(Context context) {
        super(context);
    }

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
