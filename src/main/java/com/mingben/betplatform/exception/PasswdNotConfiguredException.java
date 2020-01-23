package com.mingben.betplatform.exception;

public class PasswdNotConfiguredException extends Exception {
    public PasswdNotConfiguredException() {
        super( "密码未配置");
    }
}
