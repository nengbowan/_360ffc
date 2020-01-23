package com.mingben.betplatform.exception;

public class GetBetIssueException extends Exception {
    public GetBetIssueException(String message){
        super("获取下注期号失败 " + message);
    }
}
