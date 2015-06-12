package com.syw.weiyu.dao.user;

import android.support.annotation.NonNull;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import com.syw.weiyu.core.*;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.bean.User;
import com.syw.weiyu.bean.UserList;
import com.syw.weiyu.util.Async2Sync;
import com.syw.weiyu.util.StringUtil;
import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * author: youwei
 * date: 2015-06-03
 * desc: 用户存取类，使用Bmob存储
 */
public class UserDao4Bmob implements UserDao {
    /**
     * 创建用户数据
     * @param id
     * @param name
     * @param gender
     * @param location
     * @param listener 含bmobObjectId
     */
    @Override
    public void create(final String id, final String name, final String gender, final MLocation location, final Listener<String> listener) {
        User user = new User(id, name, gender);
        BmobGeoPoint gpsAdd = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
        user.setGpsAdd(gpsAdd);
        user.setAddressStr(location.getAddress());
        user.setLastOnlineTimestamp(System.currentTimeMillis());
        user.save(App.getCtx(), new SaveListener() {
            @Override
            public void onSuccess() {
                //get bmobObjectId
                getUserWithoutCache(id, new Listener<User>() {
                    @Override
                    public void onSuccess(User data) {
                        listener.onSuccess(data.getObjectId());
                    }

                    @Override
                    public void onFailure(String msg) {
                        listener.onFailure(msg);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure("创建用户数据出错:" + s);
            }
        });
    }

    /**
     * 更新用户资料
     * @param id
     * @param name
     * @param gender
     * @param location
     * @param listener
     */
    @Override
    public void update(@NonNull String objectId, @NonNull String id, String name, String gender, MLocation location, final Listener<Null> listener) {
        User user = new User();
        user.setObjectId(objectId);
        if (!StringUtil.isEmpty(name))user.setName(name);
        if (!StringUtil.isEmpty(gender))user.setGender(gender);
        if (location!=null) {
            BmobGeoPoint gpsAdd = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
            user.setGpsAdd(gpsAdd);
            user.setAddressStr(location.getAddress());
        }
        user.setLastOnlineTimestamp(System.currentTimeMillis());
        user.update(App.getCtx(), new UpdateListener() {
            @Override
            public void onSuccess() {
                if (listener != null) listener.onSuccess(null);
            }

            @Override
            public void onFailure(int i, String s) {
                if (listener != null) listener.onFailure("更新用户资料出错:" + s);
            }
        });
    }

    /**
     * 更新lastUpdateTimestamp
     * @param objectId
     * @param lastOnlineTimestamp
     */
    @Override
    public void updateLastOnlineTimestamp(@NonNull String objectId, @NonNull long lastOnlineTimestamp) {
        User user = new User();
        user.setObjectId(objectId);
        user.setLastOnlineTimestamp(lastOnlineTimestamp);
        user.update(App.getCtx());
    }

//    @Override
//    public void updateLocation(@NonNull String objectId, MLocation location) {
//        User user = new User();
//        user.setObjectId(objectId);
//        BmobGeoPoint gpsAdd = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
//        user.setGpsAdd(gpsAdd);
//        user.setAddressStr(location.getAddress());
//        user.update(App.getCtx());
//    }

    @Override
    public void getNearbyUsers(final MLocation location,final int pageSize, final int pageIndex, final Listener<UserList> listener) {
        BmobQuery<User> countQuery = new BmobQuery<>();
        countQuery.count(App.getCtx(), User.class, new CountListener() {
            @Override
            public void onSuccess(final int i) {
                BmobGeoPoint gpsAdd = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
                BmobQuery<User> bmobQuery = new BmobQuery<>();
//                bmobQuery.addWhereNear("gpsAdd", gpsAdd);
                bmobQuery.addWhereWithinKilometers("gpsAdd", gpsAdd, Integer.parseInt(AppConstants.default_kilometers));
                bmobQuery.order("-lastOnlineTimestamp");
                bmobQuery.setLimit(pageSize);//获取最接近用户地点的n条数据
                bmobQuery.setSkip((pageIndex - 1) * pageSize);
                bmobQuery.findObjects(App.getCtx(), new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if (list == null || i == 0) listener.onFailure("无附近的用户");
                        else listener.onSuccess(new UserList(i, list));

//                        save to db
                        FinalDb finalDb = FinalDb.create(App.getCtx());
                        for (User user : list) {
                            if (finalDb.findById(user.getId(), User.class) != null)
                                finalDb.deleteById(User.class, user.getId());
                            finalDb.save(user);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        listener.onFailure(s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(s);
            }
        });
    }

    /**
     * 获取用户数据，从本地拿，没有返回null,再从网络拿了存在本地
     * @param id
     * @return
     * @throws AppException
     */
    @Override
    public User getUser(String id) {
        FinalDb finalDb = FinalDb.create(App.getCtx());
        User user = finalDb.findById(id, User.class);
//        if (user == null) user = getUserWithoutCache(id);
        getUserWithoutCache(id, new Listener<User>() {
            @Override
            public void onSuccess(User data) {
                FinalDb finalDb = FinalDb.create(App.getCtx());
                if (finalDb.findById(data.getId(), User.class) != null) finalDb.deleteById(User.class, data.getId());
                finalDb.save(data);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
        return user;
    }

    /**
     * 通过网络拿用户数据，不走本地
     * 同步版，测试一直不通过，暂停使用
     * @param userId
     * @return
     */
    private User getUserWithoutCache(final String userId) {
        return new Async2Sync<User>(){
            @Override
            public void doSthAsync(final Listener<User> listener) {
                BmobQuery<User> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("id",userId);
                bmobQuery.setLimit(1);
                bmobQuery.findObjects(App.getCtx(), new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if (list==null || list.size()==0) {
                            listener.onFailure("无该用户信息");
                        } else {
                            listener.onSuccess(list.get(0));
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        listener.onFailure(s);
                    }
                });
            }
        }.get();
    }

    /**
     * 通过网络拿用户数据，不走本地
     * @param userId
     * @return
     */
    @Override
    public void getUserWithoutCache(final String userId, final Listener<User> listener) {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("id",userId);
        bmobQuery.setLimit(1);
        bmobQuery.findObjects(App.getCtx(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list==null || list.size()==0) {
                    listener.onFailure("无该用户信息");
                } else {
                    listener.onSuccess(list.get(0));
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onFailure(s);
            }
        });
    }
}
