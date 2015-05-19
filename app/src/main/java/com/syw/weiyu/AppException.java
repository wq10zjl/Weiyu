package com.syw.weiyu;

/**
 * author: youwei
 * date: 2015-05-12
 * desc:
 */
public class AppException extends Exception {
    public Type type;
    public AppException(Type type,String detailMessage) {
        super(detailMessage);
        this.type = type;
    }
    public AppException(String detailMessage) {
        super(detailMessage);
    }

    public enum Type {
        network,io,json,other
    }
}
