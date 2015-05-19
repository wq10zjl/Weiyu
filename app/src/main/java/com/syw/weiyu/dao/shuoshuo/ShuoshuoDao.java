package com.syw.weiyu.dao.shuoshuo;

import com.syw.weiyu.AppConstants;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.Shuoshuo;
import com.syw.weiyu.bean.ShuoshuoList;
import com.syw.weiyu.third.lbs.LBSCloud;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * author: songyouwei
 * date: 2015-05-19
 * desc:
 */
public class ShuoshuoDao {
    public ShuoshuoList getNearByShuoshuoList(int pageIndex) {
        String url = AppConstants.url_nearby_search;

        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_shuoshuo);
        params.put("q","");
        params.put("location", AppContext.getInstance().getLocation().getLongitude()+","+AppContext.getInstance().getLocation().getLatitude());
        params.put("tags","");
        params.put("radius",AppConstants.default_radius);
        //按时间|距离排序，优先显示时间靠前的
        params.put("sortby","timestamp:-1|distance:1");
        params.put("page_index",pageIndex+"");
        params.put("page_size",AppConstants.page_size_default);
        //get
        FinalHttp http = new FinalHttp();
        String result = (String) http.getSync(url, params);
        ShuoshuoList shuoshuoList = new ShuoshuoList();
        shuoshuoList.setShuoshuoList(parseFromJson(result));
        return shuoshuoList;
    }

    private List<Shuoshuo> parseFromJson (String jsonResult) {
        return null;
    }
}
