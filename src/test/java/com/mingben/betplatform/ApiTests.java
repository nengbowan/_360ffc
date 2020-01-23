package com.mingben.betplatform;

import com.alibaba.fastjson.JSON;
import com.mingben.betplatform.dto.response.issue.IssueData;
import com.mingben.betplatform.exception.*;
import org.junit.Test;

public class ApiTests {
    /**
     * 测试api
     * @param args
     * @throws LoginFailedException
     * @throws GetBalanceException
     * @throws GetBetHistoryException
     * @throws GetBetIssueException
     * @throws BetFailedException
     */
    @Test
    public static void main(String[] args) throws LoginFailedException, GetBalanceException, GetBetHistoryException, GetBetIssueException, BetFailedException {
        Api api = new Api();
        //登录　获取cookie
        api.login();
        //获取余额
        System.out.println(api.getBalance());;
        //获取下注历史
        System.out.println(JSON.toJSONString(api.getBetHistory()));
        //获取可投注期号
        IssueData issueData = api.getBetIssue();
        String issue = issueData.getCur().getIssue();
        //下注
        api.bet(issue,"元","万",15);


    }

}
