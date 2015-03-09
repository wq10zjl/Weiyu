package com.syw.weiyu.LBS;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.R;
import com.syw.weiyu.entity.MLocation;
import com.syw.weiyu.entity.User;
import com.syw.weiyu.util.ACache;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
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
    private static Context context;
    private static LBSCloud lbsCloud;
    private LBSCloud() {}
    public static void init(Context ctx) {
        context = ctx;
        if (lbsCloud == null) {
            synchronized (LBSCloud.class) {
                if (lbsCloud == null) lbsCloud = new LBSCloud();
            }
        }
    }
    public static LBSCloud getInstance() {
        return lbsCloud;
    }

    /**
     * 用户第一次使用App时，使用LBS云存储来注册用户信息
     * 用户信息和位置信息都来源于AppContext
     * 若定位失败没有POI信息，则用户的POi信息使用统一默认信息
     * @param callBack
     */
    public void registerUser(AjaxCallBack<String> callBack) {
        User user = AppContext.getInstance().getUser();
        MLocation location = AppContext.getInstance().getLocation();

        AjaxParams params = getInitializedParams();
        //user info
        params.put("userId", user.getUserId());
        params.put("name", user.getName());
        params.put("gender", user.getGender());
        params.put("hobby", user.getHobby());
        params.put("tags", user.getTags());
        params.put("organization", user.getOrganization());
        //location info
        params.put("title", user.getName());//title使用user.name
        params.put("address", location.getAddress());
        params.put("longitude", location.getLongitude());
        params.put("latitude", location.getLatitude());

        //post
        final String url = context.getString(R.string.url_create_poi);
        FinalHttp http = new FinalHttp();
        http.post(url, params, callBack);
    }

    /**
     * 异步post用户新的地理位置
     */
    public void updateUserLocation() {
        //build params
        AjaxParams params = getInitializedParams();
        params.put("userId", AppContext.getInstance().getUser().getUserId());
        params.put("latitude", AppContext.getInstance().getLocation().getLatitude());
        params.put("longitude", AppContext.getInstance().getLocation().getLongitude());
        params.put("address",AppContext.getInstance().getLocation().getAddress());

        Log.d("Weiyu","updateUserLocation params: "+params.getParamString());
        //post
        String url = context.getString(R.string.url_update_poi);
        FinalHttp http = new FinalHttp();
        http.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("Weiyu","updateUserLocation onSuccess: "+s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.d("Weiyu","updateUserLocation onFailure: "+strMsg);
            }
        });
    }

    /**
     * 更新用户资料
     * @param newUser 包含更新参数的新user
     */
    public void updateUserProfile(User newUser, AjaxCallBack<String> callBack) {
        String url = context.getString(R.string.url_update_poi);

        AjaxParams params = getInitializedParams();
        params = getUpdateParamsByCompare(params, AppContext.getInstance().getUser(), newUser);
        if (params != null) {
            Log.d("Weiyu","updateUserProfile params: "+params.getParamString());
            //post
            FinalHttp http = new FinalHttp();
            http.post(url, params, callBack);
        } else {
            Log.d("Weiyu","updateUserProfile params is null");
        }
    }

//    /**
//     * 本地检索
//     * @param q
//     * @param region
//     * @param tags
//     * @param sortby
//     * @param callBack
//     */
//    public void localSearch(String q,String region,String tags,String sortby,AjaxCallBack<String> callBack) {
//        String url = context.getString(R.string.url_local_search);
//
//        AjaxParams params = getInitializedParams();
//        params.put("q",q);
//        params.put("region",region);
//        params.put("tags",tags);
//        params.put("sortby",sortby);
//        params.put("page_size","30");
//        //get
//        FinalHttp http = new FinalHttp();
//        http.get(url, params, callBack);
//
//        Log.d("Weiyu","localSearch params: "+params.getParamString());
//    }
//
//    /**
//     * 检索本地（同城）
//     * 开始用户数量少，男女通吃，都显示
//     * @param callBack
//     */
//    public void localSearch(AjaxCallBack<String> callBack) {
//
//        localSearch(
//                null,//男女通吃
//                AppContext.getInstance().getLocation().getCity(),
//                null,
//                "distance:1",
//                callBack);
//    }

    /**
     * 周边检索
     * @param q
     * @param location
     * @param tags
     * @param radius
     * @param sortby
     * @param callBack
     */
    public void nearbySearch(String q,String location,String tags,String radius,String sortby,AjaxCallBack<String> callBack) {
        String url = context.getString(R.string.url_nearby_search);

        AjaxParams params = getInitializedParams();
        params.put("q",q);
        params.put("location",location);
        params.put("tags",tags);
        params.put("radius",radius);
        params.put("sortby",sortby);
        params.put("page_size","50");
        //get
        FinalHttp http = new FinalHttp();
        http.get(url, params, callBack);

        Log.d("Weiyu","nearbySearch params: "+params.getParamString());
    }

    /**
     * 检索附近的人
     * 检索半径配置在api_constants里
     * @param callBack
     */
    public void nearbySearch(AjaxCallBack<String> callBack) {
        nearbySearch(
                null,
                //longitude经度,latitude纬度
                AppContext.getInstance().getLocation().getLongitude()+","+AppContext.getInstance().getLocation().getLatitude(),
                null,
                context.getString(R.string.default_radius),
                "distance:1",
                callBack
        );
    }

    /**
     * 标签检索
     * @param tags
     * @param callBack
     */
    public void tagsSearch(String tags,AjaxCallBack<String> callBack) {
        String url = context.getString(R.string.url_list_poi);

        AjaxParams params = getInitializedParams();
        params.put("gender",AppContext.getInstance().getUser().getHobby());
        params.put("tags",tags);

        //get
        FinalHttp http = new FinalHttp();
        http.get(url, params, callBack);
    }


    /**
     * 公司学校检索
     * @param organization
     * @param callBack
     */
    public void organizationSearch(String organization, AjaxCallBack<String> callBack) {
        String url = context.getString(R.string.url_list_poi);

        AjaxParams params = getInitializedParams();
        params.put("gender",AppContext.getInstance().getUser().getHobby());
        params.put("organization",organization);

        //get
        FinalHttp http = new FinalHttp();
        http.get(url, params, callBack);
    }

    public void getDetail(String userId,AjaxCallBack<String> callBack) {
        String url = context.getString(R.string.url_detail_poi);

        AjaxParams params = getInitializedParams();
        params.remove("coord_type");
        params.put("userId",userId);

        //get
        FinalHttp http = new FinalHttp();
        http.get(url,params,callBack);
    }

    /**
     * 初始化API请求的固定参数：ak,geotable_id,coord_type
     * @return 返回设置好的params
     */
    private AjaxParams getInitializedParams() {
        //build params
        AjaxParams params = new AjaxParams();
        //api constants
        params.put("ak",context.getString(R.string.lbsyun_ak));
        params.put("geotable_id",context.getString(R.string.geotable_id));
        params.put("coord_type",context.getString(R.string.coord_type));
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
        originalParams.put("userId",updateUser.getUserId());
        if (currentUser.getName() != null && !currentUser.getName().equals(updateUser.getName())){
            isChanged = true;
            originalParams.put("name", updateUser.getName());
            originalParams.put("title", updateUser.getName());//title同name
        }
        if (currentUser.getGender() != null && !currentUser.getGender().equals(updateUser.getGender())){
            isChanged = true;
            originalParams.put("gender", updateUser.getGender());
        }
        if (currentUser.getHobby() != null && !currentUser.getHobby().equals(updateUser.getHobby())){
            isChanged = true;
            originalParams.put("hobby", updateUser.getHobby());
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
