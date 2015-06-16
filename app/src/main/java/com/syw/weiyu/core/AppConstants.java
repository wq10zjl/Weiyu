package com.syw.weiyu.core;

/**
 * author: youwei
 * date: 2015-05-19
 * desc: 引用配置，包括一些常量和设置的参数
 */
public class AppConstants {
    //bmob
    public static final String bmob_app_id = "9f9d56837bd74761d707beb13869ec22";
    public static final String bmob_secrect_key = "eda88bf48ee2d1a6";
    public static final String bmob_access_key = "c6148415d03e47ba42bce6157afb12ae";
    public static final String default_kilometers = "5000";

    //XGpush
    public static final long xgpush_access_id = 2100119897;
    public static final String xgpush_secret_key = "228ba691381761cfa70fd3d11c765e54";

//  Rong Cloud
    public static final String rongcloud_app_key = "uwd1c0sxdj831";
    public static final String rongcloud_app_secret = "s3RAbMx2TkaUl";
    public static final String rongcloud_app_key_debug = "pvxdm17jx5n8r";
    public static final String rongcloud_app_secret_debug = "zFfxVu2WUusvL";
//  urls
    public static final String url_user_gettoken = "https://api.cn.rong.io/user/getToken.json";
    public static final String url_user_refresh = "https://api.cn.rong.io/user/refresh.json";
    public static final String url_user_checkOnline = "https://api.cn.rong.io/user/checkOnline.json";
//  自定义的客户端常量
    public static final String RC_ACTION_RECEIVE_MESSAGE = "rc_action_receive_message";
    public static final String RC_UNREAD_COUNT = "rc_unread_count";
    //默认头像url
    public static final String url_user_icon_male = "http://com-syw-weiyu.qiniudn.com/wy_icon_male.png";
    public static final String url_user_icon_female = "http://com-syw-weiyu.qiniudn.com/wy_icon_female.png";


//  Baidu LocSDK
    public static final String locsdk_ak = "mUv2tjv4ZdI06Enf9E6GEsZ6";

//  Baidu LBSyun
    public static final String lbsyun_ak = "pzRrx7tXVcoAlxQRGAiMBfVq";
    public static final String geotable_id_user = "93391";
    public static final String geotable_id_shuoshuo = "99489";
//    public static final String geotable_id_comment = "104953";
    public static final String coord_type = "3";
//  默认的“附近”检索的半径【开始先设置5000公里，基本可以覆盖整个中国任意两点】[300公里，大约是芜湖到上海的距离]
    public static final String default_radius = "5000000";
//   默认地址信息，定位失败时使用 
    public static final String address_default = "北京市东城区东长安街";
    public static final String city_default = "北京";
    public static final String province_default = "北京";
    public static final String district_default = "东城区";
    public static final String longitude_default = "116.4038740000";
    public static final String latitude_default = "39.9148890000";
//  默认页大小
    public static final int page_size_default = 20;

//  AdsMoGo
    public static final String adsmogo_appid = "5dc8eb0ca17f4663b43dcd7f811fba95";


//  LBSyun urls
//   table options 
    public static final String url_create_table = "http://api.map.baidu.com/geodata/v3/geotable/create";
    public static final String url_list_table = "http://api.map.baidu.com/geodata/v3/geotable/list";
    public static final String url_detail_table = "http://api.map.baidu.com/geodata/v3/geotable/detail";
    public static final String url_update_table = "http://api.map.baidu.com/geodata/v3/geotable/update";
    public static final String url_delete_table = "http://api.map.baidu.com/geodata/v3/geotable/delete";

//   column options 
    public static final String url_create_column = "http://api.map.baidu.com/geodata/v3/column/create";
    public static final String url_list_column = "http://api.map.baidu.com/geodata/v3/column/list";
    public static final String url_detail_column = "http://api.map.baidu.com/geodata/v3/column/detail";
    public static final String url_update_column = "http://api.map.baidu.com/geodata/v3/column/update";
    public static final String url_delete_column = "http://api.map.baidu.com/geodata/v3/column/delete";

//   poi options 
    public static final String url_create_poi = "http://api.map.baidu.com/geodata/v3/poi/create";
    public static final String url_list_poi = "http://api.map.baidu.com/geodata/v3/poi/list";
    public static final String url_detail_poi = "http://api.map.baidu.com/geodata/v3/poi/detail";
    public static final String url_update_poi = "http://api.map.baidu.com/geodata/v3/poi/update";
    public static final String url_delete_poi = "http://api.map.baidu.com/geodata/v3/poi/delete";
    public static final String url_upload_poi = "http://api.map.baidu.com/geodata/v3/poi/upload";

//   job options 
    public static final String url_listimportdata_job = "http://api.map.baidu.com/geodata/v3/job/listimportdata";
    public static final String url_list_job = "http://api.map.baidu.com/geodata/v3/job/list";
    public static final String url_detail_job = "http://api.map.baidu.com/geodata/v3/job/detail";

//   search 
    public static final String url_local_search = "http://api.map.baidu.com/geosearch/v3/local";
    public static final String url_nearby_search = "http://api.map.baidu.com/geosearch/v3/nearby";
    public static final String url_bound_search = "http://api.map.baidu.com/geosearch/v3/bound";
    public static final String url_detail_search = "http://api.map.baidu.com/geosearch/v3/detail";
}
