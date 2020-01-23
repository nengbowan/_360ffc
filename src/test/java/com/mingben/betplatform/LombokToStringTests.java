package com.mingben.betplatform;

import com.mingben.betplatform.dto.request.LoginRequestDto;

public class LombokToStringTests {
    public static void main(String[] args) {
        String requestEntity =
                LoginRequestDto.builder()
                        .username("username")
                        .password("passwd")
                        .captcha("")
                        .code("")
                        .real_name("")
                        .newpassword("")
                        .build().toString();
        // not expected {} format
        System.out.println(requestEntity.toString());
    }
}
