package com.syw.weiyu.third.lbs;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by songyouwei on 2015/1/30.
 * 定位器
 */
public class LocSDK {

    private static LocSDK locSDK;
    private static Context context;
    private LocSDK() {}
    public static LocSDK getInstance(Context ctx) {
        context = ctx;
        if (locSDK == null) {
            synchronized (LocSDK.class) {
                if (locSDK == null) locSDK = new LocSDK();
            }
        }
        return locSDK;
    }

    /**
     * 定位
     * 不使用百度的回调接口，免去判断定位结果和停止Client的麻烦
     * 定位完成后触发回调监听
     * @param option
     * @param listener
     */
    public void locate(LocationClientOption option, final OnLocateCompleteListener listener) {
        final LocationClient mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                //这里停止Client，
                mLocationClient.stop();
                mLocationClient.unRegisterLocationListener(this);

                if (isLocateSuccess(location)) {
                    listener.onSuccess(location);
                } else {
                    listener.onFailure();
                }
            }
        });
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    /**
     * 使用默认定位选项的定位
     * 不使用百度的回调接口，免去判断定位结果和停止Client的麻烦
     * 定位完成后触发回调监听
     * @param listener
     */
    public void locate(OnLocateCompleteListener listener) {
        LocationClientOption option = new LocationClientOption();
        //低功耗模式
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        //百度经纬度坐标系 ：bd09ll
        option.setCoorType("bd09ll");
        //需要地址信息
        option.setIsNeedAddress(true);

        locate(option, listener);
    }

    /**
     * 根据locType判断定位结果
     * @param location
     * @return 定位成功与否
     */
    private boolean isLocateSuccess(BDLocation location) {
        int locType = location.getLocType();
        if (locType == BDLocation.TypeNetWorkLocation
                || locType == BDLocation.TypeOffLineLocationNetworkFail
                || locType == BDLocation.TypeOffLineLocation
                || locType == BDLocation.TypeGpsLocation) {
            return true;
        }
        else return false;
    }

    /**
     * 定位成功的回调接口
     */
    public interface OnLocateCompleteListener {
        public void onSuccess(BDLocation location);
        public void onFailure();
    }
}
