package com.syw.weiyu.util;

import android.content.Context;
import android.os.Environment;
import android.util.LruCache;
import com.jakewharton.disklrucache.DiskLruCache;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.util.ACache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * author: songyouwei
 * date: 2015-05-22
 * desc: 应用数据缓存（RAM or localFile）
 */
public class AppCacher {

    static final int cacheSize = (int) (Runtime.getRuntime().maxMemory()/2);//最大heap size的一半吧

//    private void openDiskCache(String key) {
//        DiskLruCache mDiskLruCache = null;
//        try {
//            File cacheDir = getDiskCacheDir(AppContext.getAppContext(), "Weiyu");
//            if (!cacheDir.exists()) {
//                cacheDir.mkdirs();
//            }
//            mDiskLruCache = DiskLruCache.open(cacheDir,
//                    AppContext.getAppContext().getPackageInfo().versionCode,
//                    1, cacheSize);
//            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
//            if (editor != null) {
//                OutputStream outputStream = editor.newOutputStream(0);
//                outputStream.write();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private File getDiskCacheDir(Context context, String uniqueName) {
//        String cachePath;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
//                || !Environment.isExternalStorageRemovable()) {
//            cachePath = context.getExternalCacheDir().getPath();
//        } else {
//            cachePath = context.getCacheDir().getPath();
//        }
//        return new File(cachePath + File.separator + uniqueName);
//    }

    static LruCache lruCache = new LruCache<String,Object>(cacheSize) {
//        @Override
//        protected void entryRemoved(boolean evicted, String key, Object oldValue, Object newValue) {
//            super.entryRemoved(evicted, key, oldValue, newValue);
//        }
//
//        @Override
//        protected Object create(String key) {
//            return super.create(key);
//        }
    };
    public static void put(String k,Object v) {
        lruCache.put(k,v);
    }
    public static Object get(String k) {
        return lruCache.get(k);
    }
}
