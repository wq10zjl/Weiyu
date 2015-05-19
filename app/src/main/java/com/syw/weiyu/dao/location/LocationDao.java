package com.syw.weiyu.dao.location;

import com.baidu.location.BDLocation;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.bean.MLocation;
import com.syw.weiyu.third.lbs.LocSDK;
import net.tsz.afinal.FinalDb;

/**
 * author: youwei
 * date: 2015-05-19
 * desc: 账户位置信息的存取
 */
public class LocationDao {
    public void set() {
        LocSDK.OnLocateCompleteListener locListener = new LocSDK.OnLocateCompleteListener(){
            @Override
            public void onSuccess(BDLocation location) {
                FinalDb.create(AppContext.getCtx()).save(new MLocation(location));
            }

            @Override
            public void onFailure() {
                FinalDb.create(AppContext.getCtx()).save(new MLocation(null));
            }
        };
        LocSDK.getInstance().locate(locListener);
    }

    public MLocation get() {
        MLocation location = FinalDb.create(AppContext.getCtx()).findAll(MLocation.class).get(0);
        if (location == null) {
            location = new MLocation(null);
        }
        return location;
    }

    public void update() {
        LocSDK.OnLocateCompleteListener locListener = new LocSDK.OnLocateCompleteListener(){
            @Override
            public void onSuccess(BDLocation location) {
                FinalDb.create(AppContext.getCtx()).update(new MLocation(location));
            }

            @Override
            public void onFailure() {
                FinalDb.create(AppContext.getCtx()).update(new MLocation(null));
            }
        };
        LocSDK.getInstance().locate(locListener);
    }
}