package com.syw.weiyu.dao.img;

import android.content.Context;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.LocalThumbnailListener;
import com.bmob.btp.callback.UploadListener;
import com.orhanobut.logger.Logger;
import com.syw.weiyu.core.AppConstants;
import com.syw.weiyu.core.Listener;

/**
 * author: youwei
 * date: 2015-06-16
 * desc:
 */
public class BmobImageDao {
    private static BmobProFile bmobProFile;
    private static BmobImageDao imageDao;
    private BmobImageDao() {}
    public static BmobImageDao getInstance(Context ctx) {
        bmobProFile = BmobProFile.getInstance(ctx);
        if (imageDao == null) {
            synchronized (BmobImageDao.class) {
                if (imageDao == null) imageDao = new BmobImageDao();
            }
        }
        return imageDao;
    }
    public void save(String filePath, final Listener<String> listener) {
        bmobProFile.upload(filePath, new UploadListener() {

            //文件名（带后缀），这个文件名是唯一的，开发者需要记录下该文件名，方便后续下载或者进行缩略图的处理。
            //url：文件服务器地址（如果你上传的是图片类型的文件，此url地址并不能直接在浏览器查看（会出现404错误），
            // 这个url地址需要经过URL签名后才可以被访问到，而URL签名又分开启和不开启两种方式，二者的区别在于时效性。具体可查看URL签名
            //未开启签名认证的URL地址格式：
            // URL=url(文件上传成功之后返回的url地址)?t=（客户端类型）&a=(应用密钥中的AccessKey)
            @Override
            public void onSuccess(String fileName, String url) {
                String signedURL = bmobProFile.signURL(fileName, url, AppConstants.bmob_access_key, 0, null);
                Logger.i("signedURL:" + signedURL);
                listener.onSuccess(signedURL);
            }

            @Override
            public void onProgress(int ratio) {
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                listener.onFailure("上传出错：" + errormsg);
            }
        });
    }

    public void getThumbnail(String filePath, final Listener<String> listener) {
        bmobProFile.getLocalThumbnail(filePath, 1, new LocalThumbnailListener() {

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Logger.i("MainActivity -localThumbnail-->生成缩略图失败 :" + statuscode + "," + errormsg);
                listener.onFailure(errormsg);
            }

            @Override
            public void onSuccess(String thumbnailPath) {
                // TODO Auto-generated method stub
                Logger.i("MainActivity -localThumbnail-->生成后的缩略图路径 :" + thumbnailPath);
                listener.onSuccess(thumbnailPath);
            }
        });
    }
}
