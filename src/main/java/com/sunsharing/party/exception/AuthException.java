package com.sunsharing.party.exception;

/**
 * Created with IntelliJ IDEA.
 * User: yl
 * Date: 12-12-26
 * Time: 上午11:16
 * 请求未登录时错误抛出的异常
 */
public class AuthException extends Exception {

    private String accessURL = "/";

    public AuthException(Throwable root) {
        super(root);
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message,String accessURL) {
        super(message);
        this.accessURL = accessURL;
    }

    public String getAccessURL() {
        return accessURL;
    }

    public void setAccessURL(String accessURL) {
        this.accessURL = accessURL;
    }
}
