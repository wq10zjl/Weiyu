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

    /**
     * 获取签名后的可直接访问的图片地址
     * @param unsignedUrl
     * @return
     */
    public static String getSingedUrl(String unsignedUrl) {
        return unsignedUrl+"?t=2&a="+AppConstants.bmob_access_key;
    }

    /**
     * 保存图片到服务器
     * @param filePath 本地图片路径
     * @param listener 包含返回的未签名的图片访问地址
     */
    public void savePic(String filePath, final Listener<String> listener) {
        bmobProFile.upload(filePath, new UploadListener() {

            //文件名（带后缀），这个文件名是唯一的，开发者需要记录下该文件名，方便后续下载或者进行缩略图的处理。
            //url：文件服务器地址（如果你上传的是图片类型的文件，此url地址并不能直接在浏览器查看（会出现404错误），
            // 这个url地址需要经过URL签名后才可以被访问到，而URL签名又分开启和不开启两种方式，二者的区别在于时效性。具体可查看URL签名
            //未开启签名认证的URL地址格式：
            // URL=url(文件上传成功之后返回的url地址)?t=（客户端类型,Android为2）&a=(应用密钥中的AccessKey)
            @Override
            public void onSuccess(String fileName, String url) {
                Logger.i("upload file success, fileName:" + fileName + ", url:" + url + ", signedURL:" + getSingedUrl(url));
                listener.onSuccess(url);
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

    /**
     * 保存图片到服务器（经过压缩处理）
     * @param filePath 本地图片路径
     * @param listener 包含返回的未签名的图片访问地址
     */
    public void saveThumbnailPic(String filePath, final Listener<String> listener) {
        getLocalThumbnail(filePath, new Listener<String>() {
            @Override
            public void onSuccess(String data) {
                savePic(data,listener);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 本地生成缩略图
     * @param filePath
     * @param listener 包含生成后的缩略图路径
     */
    private void getLocalThumbnail(String filePath, final Listener<String> listener) {
        bmobProFile.getLocalThumbnail(filePath, 1, new LocalThumbnailListener() {

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Logger.i("localThumbnail-->生成缩略图失败 :" + statuscode + "," + errormsg);
                listener.onFailure("生成缩略图失败:"+errormsg);
            }

            @Override
            public void onSuccess(String thumbnailPath) {
                // TODO Auto-generated method stub
                Logger.i("localThumbnail-->生成后的缩略图路径 :" + thumbnailPath);
                listener.onSuccess(thumbnailPath);
            }
        });
    }
}
