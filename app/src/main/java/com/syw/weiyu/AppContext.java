package com.syw.weiyu;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.dao.location.LocationDao;
import com.syw.weiyu.dao.user.AccountDao;
import com.syw.weiyu.third.lbs.LBSCloud;
import com.syw.weiyu.third.lbs.LocSDK;
import com.syw.weiyu.third.im.RongCloud;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.util.ACache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by songyouwei on 2015/2/10.
 * 应用的单例全局上下文，在App开启时被初始化
 * 主要用来加载并缓存一些运行时数据
 */
public final class AppContext {

    //'key's store values in local
    public static final String USER = "user";
    public static final String LOCATION = "location";
    public static final String TOKEN = "token";

    private static Context context;
    private static AppContext self;
    private AppContext() {}
    public static AppContext getInstance() {
        return self;
    }


    /**
     * 初始化AppContext
     */
    public static void init(Context ctx) {
        Logger.d("init AppContext");
        context = ctx;
        if (self == null) {
            synchronized (AppContext.class) {
                if (self == null) self = new AppContext();
            }
        }
    }

    public static Context getCtx() {
        return context;
    }


    /**
     * 读取并缓存运行时数据
     * 从本地初始化,若不为空，则缓存进AppContext中
     * User信息（不包含location信息）
     * Token信息
     * Location信息
     */
    public void initilize() {
        //只初始化地理信息
        new LocationDao().set();

//        //initilize user&token&connect only if the user isn't null
//        User user = (User) ACache.getPermanence(context).getAsObject(USER);
//        if (user != null) {
//            //initilize user
//            setUser(user);
//            //initilize token
//            setToken(ACache.getPermanence(context).getAsString("token"));
//            //connect rong cloud
//            RongCloud.getInstance(context).connectRongCloud();
//        }
//        //initilize location
//        initLocation();
    }

    /**
     * 定位，初始化位置信息
     * 若成功，保存位置信息，
     * 又若已有用户，更新POI信息
     * 若失败，且本地没有位置数据，则使用默认的未知数据（北京）
     */
    private void initLocation() {
        LocSDK.getInstance().locate(new LocSDK.OnLocateCompleteListener() {
            MLocation mLocation;
            @Override
            public void onSuccess(BDLocation location) {
                mLocation = new MLocation(location);
                Log.d("Weiyu","initLocation success :"+mLocation);

                //set in ram
                setLocation(mLocation);
                //set in sd card
                ACache.get(context).put(LOCATION,mLocation);
                //update user location in lbs cloud
                if (user != null) {
                    LBSCloud.getInstance().updateUserLocation();
                }
            }

            @Override
            public void onFailure() {
                //若定位失败，尝试从本地读取
                mLocation = (MLocation) ACache.get(context).getAsObject(LOCATION);
                //若本地没有存储的位置信息，构建默认的位置（北京）
                //不保存在本地，只存放在ram中
                if (mLocation == null) {
                    mLocation = new MLocation(null);
                }
            }
        });
    }

    /**
     * 当前使用App的用户
     */
    private User user;

    /**
     * 当前用户的位置,默认“北京”
     */
    private MLocation location = new MLocation(null);

    /**
     * 当前用户的token
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MLocation getLocation() {
        return location;
    }

    public void setLocation(MLocation location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    /**
     * Nearby视图中的数据
     */
    //用于视图适配器的mapList
    List<HashMap<String, String>> usermapList = new ArrayList<>();
    //这个UserList只放userId&name，用于开启聊天
    List<User> userList = new ArrayList<>();

    /**
     * 附近的说说视图中的数据
     */
    //用于视图适配器的mapList
    List<HashMap<String, String>> shuoshuomapList = new ArrayList<>();

    public List<HashMap<String, String>> getShuoshuomapList() {
        return shuoshuomapList;
    }

    public void setShuoshuomapList(List<HashMap<String, String>> shuoshuomapList) {
        this.shuoshuomapList = shuoshuomapList;
    }

    public List<HashMap<String, String>> getUsermapList() {
        return usermapList;
    }

    public void setUsermapList(List<HashMap<String, String>> usermapList) {
        this.usermapList = usermapList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
