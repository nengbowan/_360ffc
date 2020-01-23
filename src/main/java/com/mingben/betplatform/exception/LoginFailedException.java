package com.mingben.betplatform.exception;

public class LoginFailedException extends Exception {
    public LoginFailedException(String message){
        super("登陆失败 "+ message);
    }
}
