package com.mingben.betplatform.exception;

public class BankCardNameNotConfiguredException extends Exception {
    public BankCardNameNotConfiguredException() {
        super( "银行卡名称未配置");
    }
}
