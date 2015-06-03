package com.syw.weiyu.util;

import com.syw.weiyu.api.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * author: youwei
 * date: 2015-06-03
 * desc: 异步转同步的工具
 */
public abstract class Async2Sync<T> {

    /**
     * 获取异步方法中返回的数据
     * @return
     */
    public final T get() {
        final List<T> list = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(2);
        Thread asyncTask = new Thread(){
            @Override
            public void run(){
                // do something
                doSthAsync(new Listener<T>() {
                    @Override
                    public void onSuccess(T data) {
                        list.add(data);
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(String msg) {
                        latch.countDown();
                    }
                });
            }
        };
        asyncTask.start();
        latch.countDown();

        return list.get(0);
    }

    /**
     * 异步方法
     * @param listener
     */
    public abstract void doSthAsync(Listener<T> listener);
}
