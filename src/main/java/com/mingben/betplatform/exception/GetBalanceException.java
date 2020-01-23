package com.mingben.betplatform.exception;

public class GetBalanceException extends Exception {
    public GetBalanceException(String message){
        super("获取账户余额失败 " + message);
    }
}
