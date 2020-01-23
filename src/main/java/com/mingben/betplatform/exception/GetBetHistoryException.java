package com.mingben.betplatform.exception;

public class GetBetHistoryException extends Exception {
    public GetBetHistoryException(String message){
        super("获取下注历史失败 " + message);
    }
}
