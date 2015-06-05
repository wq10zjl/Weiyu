package com.syw.weiyu.util;

import com.syw.weiyu.core.Listener;

import java.util.concurrent.CountDownLatch;

/**
 * author: youwei
 * date: 2015-06-03
 * desc: 异步转同步的工具
 */
public abstract class Async2Sync<T> {

    private T t = null;
    private CountDownLatch latch = new CountDownLatch(1);
    private Listener<T> listener = new Listener<T>() {
        @Override
        public void onSuccess(T data) {
            t = data;
            latch.countDown();
        }

        @Override
        public void onFailure(String msg) {
            latch.countDown();
        }
    };

    /**
     * 获取异步方法中返回的数据
     * @return
     */
    public final T get() {
        new Runnable(){
            @Override
            public void run(){
                // do something
                doSthAsync(listener);
            }
        }.run();
        return t;
    }

    /**
     * 异步方法
     * @param listener
     */
    public abstract void doSthAsync(Listener<T> listener);
}
