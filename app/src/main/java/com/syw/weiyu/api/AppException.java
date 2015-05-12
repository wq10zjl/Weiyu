package com.syw.weiyu.api;

/**
 * author: youwei
 * date: 2015-05-12
 * desc:
 */
public class AppException extends Exception {
    public AppException(Type type,String detailMessage) {
        super(detailMessage);
    }

    public enum Type {
        network,io,json,other
    }
}
