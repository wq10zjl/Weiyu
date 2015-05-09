package com.syw.weiyu.util;

import com.syw.weiyu.App;
import com.syw.weiyu.api.ad.AdApi;
import com.syw.weiyu.api.ad.impl.MoGo;
import com.syw.weiyu.ui.main.LauncherActivity;
import com.syw.weiyu.ui.main.ShuoshuoActivity;
import com.syw.weiyu.ui.main.explore.NearByUserActivity;

import dagger.Module;
import dagger.Provides;

/**
 * author: songyouwei
 * date: 2015-05-08
 * desc: app main module for dagger ioc
 */
@Module(
        injects = {
                LauncherActivity.class,
                ShuoshuoActivity.class,
                NearByUserActivity.class,
        }
)
public class AppModule {

    @Provides
    AdApi provideAdApi() {
        return new MoGo();
    }
}
