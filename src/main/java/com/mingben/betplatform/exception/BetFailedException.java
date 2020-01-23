package com.mingben.betplatform.exception;

public class BetFailedException extends Exception {
    public BetFailedException(String message){
        super("下注失败 "+ message);
    }
}
