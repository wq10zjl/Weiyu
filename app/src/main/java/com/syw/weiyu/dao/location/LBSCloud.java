package com.syw.weiyu.dao.location;

import com.syw.weiyu.core.AppConstants;
import com.syw.weiyu.bean.User;

import net.tsz.afinal.http.AjaxParams;

/**
 * Created by songyouwei on 2015/1/30.
 * LBS云：
 * 云存储（用户注册，资料更新，位置更新）
 * 云检索（附近、同城、标签搜索、公司学校搜索）
 * 注：所有检索的共同条件是只检索自己的“爱好”，即gender=AppContext.getUser().getHobby()
 * 官方SDK不好用，自己封装请求和解析返回数据
 */
public class LBSCloud {
//    private static Context context;
//    private static LBSCloud lbsCloud;
//    private LBSCloud() {}
//    public static void init(Context ctx) {
//        context = ctx;
//        if (lbsCloud == null) {
//            synchronized (LBSCloud.class) {
//                if (lbsCloud == null) lbsCloud = new LBSCloud();
//            }
//        }
//    }
//    public static LBSCloud getInstance() {
//        return lbsCloud;
//    }
//
//    /**
//     * 用户第一次使用App时，使用LBS云存储来注册用户信息
//     * 用户信息和位置信息都来源于AppContext
//     * 若定位失败没有POI信息，则用户的POi信息使用统一默认信息
//     * @param callBack
//     */
//    public void registerUser(AjaxCallBack<String> callBack) {
//        User user = AppContext.getInstance().getUser();
//        MLocation location = AppContext.getInstance().getLocation();
//
//        AjaxParams params = getInitializedParams();
//        //user info
//        params.putCache("userId", user.getUid());
//        params.putCache("name", user.getName());
//        params.putCache("gender", user.getGender());
//        params.putCache("tags", user.getTags());
//        params.putCache("organization", user.getOrganization());
//        //location info
//        params.putCache("title", user.getName());//title使用user.name
//        params.putCache("address", location.getAddress());
//        params.putCache("longitude", location.getLongitude());
//        params.putCache("latitude", location.getLatitude());
//
//        //post
//        final String url = context.getString(R.string.url_create_poi);
//        FinalHttp http = new FinalHttp();
//        http.post(url, params, callBack);
//    }
//
//    /**
//     * 异步post用户新的地理位置
//     */
//    public void updateUserLocation() {
//        //build params
//        AjaxParams params = getInitializedParams();
//        params.putCache("userId", AppContext.getInstance().getUser().getUid());
//        params.putCache("latitude", AppContext.getInstance().getLocation().getLatitude());
//        params.putCache("longitude", AppContext.getInstance().getLocation().getLongitude());
//        params.putCache("address",AppContext.getInstance().getLocation().getAddress());
//
//        Log.d("Weiyu", "updateUserLocation params: " + params.getParamString());
//        //post
//        String url = context.getString(R.string.url_update_poi);
//        FinalHttp http = new FinalHttp();
//        http.post(url, params, new AjaxCallBack<String>() {
//            @Override
//            public void onSuccess(String s) {
//                Log.d("Weiyu","updateUserLocation onSuccess: "+s);
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                Log.d("Weiyu","updateUserLocation onFailure: "+strMsg);
//            }
//        });
//    }
//
//    /**
//     * 异步更新最后在线时间
//     */
//    public void updateLastOnlineTime() {
//        //build params
//        AjaxParams params = getInitializedParams();
//        params.putCache("userId", AppContext.getInstance().getUser().getUid());
//        //post
//        String url = context.getString(R.string.url_update_poi);
//        FinalHttp http = new FinalHttp();
//        http.post(url, params, new AjaxCallBack<String>() {
//            @Override
//            public void onSuccess(String s) {
//                Log.d("Weiyu", "updateUserLocation onSuccess: " + s);
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                Log.d("Weiyu", "updateUserLocation onFailure: " + strMsg);
//            }
//        });
//    }
//
//    /**
//     * 更新用户资料
//     * @param newUser 包含更新参数的新user
//     */
//    public void updateUserProfile(User newUser, AjaxCallBack<String> callBack) {
//        String url = context.getString(R.string.url_update_poi);
//
//        AjaxParams params = getInitializedParams();
//        params = getUpdateParamsByCompare(params, AppContext.getInstance().getUser(), newUser);
//        if (params != null) {
//            Log.d("Weiyu","updateUserProfile params: "+params.getParamString());
//            //post
//            FinalHttp http = new FinalHttp();
//            http.post(url, params, callBack);
//        } else {
//            Log.d("Weiyu","updateUserProfile params is null");
//        }
//    }
//
//    /**
//     * 发布说说
//     * @param content
//     */
//    public void publishShuoshuo(String content, AjaxCallBack<String> callBack) {
//        User user = AppContext.getInstance().getUser();
//        MLocation location = AppContext.getInstance().getLocation();
//
//        AjaxParams params = getInitializedParams(context.getString(R.string.geotable_id_shuoshuo));
//        //user info
//        params.putCache("userId", user.getUid());
//        params.putCache("userName", user.getName());
//        //location info
//        params.putCache("longitude", location.getLongitude());
//        params.putCache("latitude", location.getLatitude());
//        //content
//        params.putCache("content",content);
//        //timestamp, same as create_time
//        params.putCache("timestamp",System.currentTimeMillis()+"");
//
//        //post
//        final String url = context.getString(R.string.url_create_poi);
//        FinalHttp http = new FinalHttp();
//        http.post(url, params, callBack);
//
//        Log.d("Weiyu","publish shuoshuo params: "+params.getParamString());
//    }
//
//
//    /**
//     * 检索附近的人
//     * @param q
//     * @param location
//     * @param tags
//     * @param radius
//     * @param sortby
//     * @param pageIndex 页码，从0开始
//     * @param callBack
//     */
//    public void nearbyUserSearch(String q, String location, String tags, String radius, String sortby,int pageIndex, AjaxCallBack<String> callBack) {
//        String url = context.getString(R.string.url_nearby_search);
//
//        AjaxParams params = getInitializedParams();
//        params.putCache("q",q);
//        params.putCache("location",location);
//        params.putCache("tags",tags);
//        params.putCache("radius",radius);
//        params.putCache("sortby",sortby);
//        params.putCache("page_index",pageIndex+"");
//        params.putCache("page_size",context.getString(R.string.page_size_default));
//        //getCache
//        FinalHttp http = new FinalHttp();
//        http.getCache(url, params, callBack);
//
//        Log.d("Weiyu","nearbyUserSearch params: "+params.getParamString());
//    }
//
//    /**
//     * 检索附近的人
//     * 检索半径配置在api_constants里
//     * @param callBack
//     */
//    public void nearbyUserSearch(int pageIndex, AjaxCallBack<String> callBack) {
//        nearbyUserSearch(
//                null,
//                //longitude经度,latitude纬度
//                AppContext.getInstance().getLocation().getLongitude() + "," + AppContext.getInstance().getLocation().getLatitude(),
//                null,
//                context.getString(R.string.default_radius),
//                "distance:1",
//                pageIndex,
//                callBack
//        );
//    }
//
//    /**
//     * 检索附近的说说
//     * 检索半径配置在api_constants里
//     * @param callBack
//     */
//    public void nearbyShuoshuoSearch(int pageIndex, AjaxCallBack<String> callBack) {
//        String url = context.getString(R.string.url_nearby_search);
//
//        AjaxParams params = getInitializedParams(context.getString(R.string.geotable_id_shuoshuo));
//        params.putCache("q","");
//        params.putCache("location",AppContext.getInstance().getLocation().getLongitude()+","+AppContext.getInstance().getLocation().getLatitude());
//        params.putCache("tags","");
//        params.putCache("radius",context.getString(R.string.default_radius));
//        //按时间|距离排序，优先显示时间靠前的
//        params.putCache("sortby","timestamp:-1|distance:1");
//        params.putCache("page_index",pageIndex+"");
//        params.putCache("page_size",context.getString(R.string.page_size_default));
//        //getCache
//        FinalHttp http = new FinalHttp();
//        http.getCache(url, params, callBack);
//
//        Log.d("Weiyu","nearbyShuoshuoSearch params: "+params.getParamString());
//    }
//
//
//    /**
//     * 标签检索
//     * @param tags
//     * @param callBack
//     */
//    public void tagsSearch(String tags,AjaxCallBack<String> callBack) {
//        String url = context.getString(R.string.url_list_poi);
//
//        AjaxParams params = getInitializedParams();
//        params.putCache("tags",tags);
//
//        //getCache
//        FinalHttp http = new FinalHttp();
//        http.getCache(url, params, callBack);
//    }
//
//
//    /**
//     * 公司学校检索
//     * @param organization
//     * @param callBack
//     */
//    public void organizationSearch(String organization, AjaxCallBack<String> callBack) {
//        String url = context.getString(R.string.url_list_poi);
//
//        AjaxParams params = getInitializedParams();
//        params.putCache("organization",organization);
//
//        //getCache
//        FinalHttp http = new FinalHttp();
//        http.getCache(url, params, callBack);
//    }
//
//    public void getDetail(String userId,AjaxCallBack<String> callBack) {
//        String url = context.getString(R.string.url_detail_poi);
//
//        AjaxParams params = getInitializedParams();
//        params.remove("coord_type");
//        params.putCache("userId",userId);
//
//        //getCache
//        FinalHttp http = new FinalHttp();
//        http.getCache(url,params,callBack);
//    }
//
//    /**
//     * 初始化API请求的固定参数：ak,geotable_id,coord_type
//     * @return 返回设置好的params
//     */
//    private AjaxParams getInitializedParams() {
//        return getInitializedParams(context.getString(R.string.geotable_id_user));
//    }

    /**
     * 初始化API请求的固定参数：ak,geotable_id,coord_type
     * @param geotable_id 指定数据表的ID
     * @return 返回设置好的params
     */
    public static AjaxParams getInitializedParams(String geotable_id) {
        //build params
        AjaxParams params = new AjaxParams();
        //api constants
        params.put("ak", AppConstants.lbsyun_ak);
        params.put("geotable_id",geotable_id);
        params.put("coord_type",AppConstants.coord_type);
        return params;
    }

    /**
     * 更新用户信息时，比较发生变化的字段
     * 若有变化字段，返回params
     * 否则，返回null
     * @param currentUser 当前用户
     * @param updateUser 要更新的用户信息
     * @param originalParams 原来的参数
     * @return
     */
    private AjaxParams getUpdateParamsByCompare(AjaxParams originalParams, User currentUser,  User updateUser) {
        boolean isChanged = false;
        originalParams.put("userId",updateUser.getId());
        if (currentUser.getName() != null && !currentUser.getName().equals(updateUser.getName())){
            isChanged = true;
            originalParams.put("name", updateUser.getName());
            originalParams.put("title", updateUser.getName());//title同name
        }
        if (currentUser.getGender() != null && !currentUser.getGender().equals(updateUser.getGender())){
            isChanged = true;
            originalParams.put("gender", updateUser.getGender());
        }
        if (currentUser.getTags() != null && !currentUser.getTags().equals(updateUser.getTags())){
            isChanged = true;
            originalParams.put("tags", updateUser.getTags());
        }
        if (currentUser.getOrganization() !=null && !currentUser.getOrganization().equals(updateUser.getOrganization())){
            isChanged = true;
            originalParams.put("organization", updateUser.getOrganization());
        }
        return isChanged?originalParams:null;
    }

}
