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
    /**
     * 获取异步方法中返回的数据
     * @return
     */
    public final T get() {
        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch doneSignal = new CountDownLatch(1);
        Thread wordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startSignal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                // do something
                doSthAsync(new Listener<T>() {
                    @Override
                    public void onSuccess(T data) {
                        t = data;
                        doneSignal.countDown();
                    }

                    @Override
                    public void onFailure(String msg) {
                        doneSignal.countDown();
                    }
                });
            }
        });
        wordThread.start();
        startSignal.countDown();
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 异步方法
     * @param listener
     */
    public abstract void doSthAsync(Listener<T> listener);
}
