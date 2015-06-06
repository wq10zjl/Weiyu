package com.syw.weiyu.dao.location;

import com.baidu.location.BDLocation;
import com.syw.weiyu.core.AppContext;
import com.syw.weiyu.bean.MLocation;
import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * author: youwei
 * date: 2015-05-19
 * desc: 账户位置信息的存取
 */
public class LocationDao {

    /**
     * 定位，并保存or更新位置信息
     */
    public void set() {
        LocSDK.OnLocateCompleteListener locListener = new LocSDK.OnLocateCompleteListener(){
            @Override
            public void onSuccess(BDLocation location) {
                MLocation mLocation = new MLocation(location);
                FinalDb finalDb = FinalDb.create(AppContext.getCtx());
                finalDb.deleteAll(MLocation.class);
                finalDb.save(mLocation);
                AppContext.putCache(AppContext.KEY_LOCATION, mLocation);
            }

            @Override
            public void onFailure() {
                //do nothing
            }
        };
        LocSDK.getInstance().locate(locListener);
    }

    /**
     * 获取保存的位置数据
     * 如果不存在数据，则构造默认的位置并返回
     * @return
     */
    public MLocation get() {
        MLocation location = (MLocation) AppContext.getCache(AppContext.KEY_LOCATION);
        if (location == null) {
            FinalDb finalDb = FinalDb.create(AppContext.getCtx());
            List<MLocation> locations = finalDb.findAll(MLocation.class);
            if (locations == null || locations.size() == 0) {
                location = new MLocation(null);
            } else {
                location = locations.get(0);
            }
        }
        return location;
    }
}