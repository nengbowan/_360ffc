package com.mingben.betplatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mingben.betplatform.config.BetConfig;
import com.mingben.betplatform.dto.response.BetHistory;
import com.mingben.betplatform.dto.request.BetRequestDto;
import com.mingben.betplatform.dto.request.LoginRequestDto;
import com.mingben.betplatform.dto.response.issue.IssueData;
import com.mingben.betplatform.exception.*;
import com.mingben.betplatform.util.CollectionUtils;
import com.mingben.betplatform.util.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

public class Api {
    private static Logger logger = LoggerFactory.getLogger(Api.class);
    //cookie auto manager
    private CookieStore cookieStore = new BasicCookieStore();
    private CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

    private String baseURL = BetConfig.SINGLE.getWebSite();

    private Charset defaultCharset = Charset.forName("utf8");
    /**
     * 登录api
     * etc.  {"username":"jockie00","password":"jockie00","captcha":"","code":"","real_name":"","newpassword":""}
     */
    public void login()throws LoginFailedException{
        String url = "/login";
        String requestUrl = baseURL + url;
        String requestEntity =
                null
                ;
        try {
            requestEntity = JSON.toJSONString(LoginRequestDto.builder()
                    .username(BetConfig.SINGLE.getUsername())
                    .password(BetConfig.SINGLE.getPasswd())
                    .captcha("")
                    .code("")
                    .real_name(BetConfig.SINGLE.getBankCardName())
                    .newpassword("")
                    .build());
        } catch (BankCardNameNotConfiguredException e) {
            throw new LoginFailedException("登陆失败　银行卡名称未配置,否则会因为异地登录失败");
        } catch (UsernameNotConfiguredException e) {
            throw new LoginFailedException("登陆失败　用户名未配置");
        } catch (PasswdNotConfiguredException e) {
            throw new LoginFailedException("登陆失败　密码未配置");
        }
        HttpPost post = new HttpPost(requestUrl);
        StringEntity json = new StringEntity(requestEntity, ContentType.APPLICATION_JSON);
        post.setEntity(json);
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            String respStr = EntityUtils.toString(response.getEntity(),defaultCharset);
            if(respStr.contains("\\u94f6\\u884c\\u5361\\u59d3\\u540d\\u4e0d\\u6b63\\u786e\\uff01")){
                throw new LoginFailedException("银行卡姓名不正确！");
            }
            if(!respStr.contains("\"status\":0")){
                throw new LoginFailedException(respStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //检测是否有给token TODO

    }

    /**
     * 下注
     * @game cxg360ffc qiqutxffssc
     * @return
     */
    public void bet(String game , String issue , String betMoneyUnit , String betWhere,int betMultiple)throws BetFailedException{
        String url = "/lottery/bet?ident="+game;
        String requestUrl = baseURL + url;
        HttpPost post = new HttpPost(requestUrl);
        // parameter SerializerFeature.WriteMapNullValue for null value , but property must be exist
        // property position must be exist
        String requestEntity =
                JSON.toJSONString(
                    BetRequestDto.generate(game , issue , betMoneyUnit , betWhere , betMultiple) , SerializerFeature.WriteMapNullValue
                );
        StringEntity json = new StringEntity(requestEntity, ContentType.APPLICATION_JSON);
        post.setEntity(json);
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            String respStr = EntityUtils.toString(response.getEntity(),defaultCharset);
            if(respStr.contains("\\u6700\\u4f4e\\u6295\\u6ce8\\u91d1\\u989d 0.01 \\u5143\\uff01")){
                throw new BetFailedException("最低投注金额 0.01 元！");
            }
            if(respStr.contains("\\u6295\\u6ce8\\u6210\\u672c\\u4e0d\\u6b63\\u786e")){
                throw new BetFailedException("投注成本不正确");
            }
            if(!respStr.contains("\"status\":0")){
                throw new BetFailedException(respStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取下注历史
     * @return
     */
    public List<BetHistory> getBetHistory()throws GetBetHistoryException{
        String url = "/lottery/historybet?ident=cxg360ffc";
        String requestUrl = baseURL + url;
        HttpGet get = new HttpGet(requestUrl);
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            String respStr = EntityUtils.toString(response.getEntity(),defaultCharset);
            if(!respStr.contains("\"status\":0")){
                throw new GetBetHistoryException(respStr);
            }
            List<BetHistory> result = JSONObject.parseArray(
                    JSONObject.parseObject(respStr).getJSONArray("data").toJSONString()
                    ,BetHistory.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据期数查询下注历史
     * @param issue must not be null
     * @return
     */
    public BetHistory getBetHistoryByIssue(String issue){
        if(StringUtils.isEmpty(issue)){
            throw new IllegalArgumentException("issue must not be null");
        }
        try {
            List<BetHistory> betHistories = getBetHistory();
            if(CollectionUtils.isEmpty(betHistories)){
                return null;
            }
            for(BetHistory cur : betHistories){
                if(issue.equals(cur.getIssue())){
                    return cur;
                }
            }
        } catch (GetBetHistoryException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 获取余额
     * @return
     */
    public Double getBalance()throws GetBalanceException {
        String url = "/user/balance";
        String requestUrl = baseURL + url;
        HttpGet get = new HttpGet(requestUrl);
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            String respStr = EntityUtils.toString(response.getEntity(),defaultCharset);
            if(!respStr.contains("\"status\":0")){
                throw new GetBalanceException(respStr);
            }
            Double result = ((JSONObject)JSONObject.parseObject(respStr).get("data")).getDouble("balance");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前可投注期号
     * @return
     */
    public IssueData getBetIssue()throws GetBetIssueException{
        String url = "/lottery/issue?ident=cxg360ffc";
        String requestUrl = baseURL + url;
        HttpGet get = new HttpGet(requestUrl);
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            String respStr = EntityUtils.toString(response.getEntity(),defaultCharset);
            if(!respStr.contains("\"status\":0")){
                throw new GetBetIssueException(respStr);
            }
            IssueData result = JSONObject.parseObject(respStr).getObject("data",IssueData.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 主方法
     * @param args
     */
    public static void _360ffc(String[] args) throws UsernameNotConfiguredException, PasswdNotConfiguredException, LoginFailedException {
        Api api = new Api();
        api.login();
        logger.info("登陆成功....");

        //投注失败　一直投注

        int failCount = 0;



        //损失掉的钱
        int lostMoney = 0;

        //利润
        float sumMoney = 0;
        //输一次　就清零
        int newPlanCount = 0;
        //只算同一个批次里面的成功个数
        int successCount = 0;
        try {

            //永远执行

            int multiple = BetConfig.SINGLE.getStartBetMoney();


            while(true){
                try {
                    Double balance = api.getBalance();
                    logger.info(" ============= 账户余额 {}" , balance);
                } catch (GetBalanceException e) {
                    e.printStackTrace();
                }
                boolean betSuccess = false;


                String betIssue = null;
                while (!betSuccess){
                    //下注
                    try {

                        IssueData issueAvailable = api.getBetIssue();
                        String issue = issueAvailable.getCur().getIssue();
                        logger.info("获取期号成功　{}" , issue);


                        //下注万位 1234567位　各1厘
                        api.bet("cxg360ffc",issue,BetConfig.SINGLE.getBetMoneyUnit(),BetConfig.SINGLE.getBetWhere(),multiple);
                        logger.info("下注成功 -  {} - {}位 1,2,3,4,5,6,7 - {}{}" , issue , BetConfig.SINGLE.getBetWhere()  , multiple , BetConfig.SINGLE.getBetMoneyUnit());
                        betSuccess = true;

                        betIssue = issue;
                    } catch (BetFailedException e) {
                        logger.error(e.getMessage());
                        try {
                            logger.info("三秒后再次投注.");
                            Thread.sleep(3000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                try {
                    Double balance = api.getBalance();
                    logger.info(" ============= 账户余额 {}" , balance);
                } catch (GetBalanceException e) {
                    e.printStackTrace();
                }


                //获取下注历史
                //先判断时间　未开奖
                //0 未开奖 1 中奖了　2　未中奖
                //是否已结算
                int hasCalculate = 0;
                while(hasCalculate == 0){
                    BetHistory history = api.getBetHistoryByIssue(betIssue);
                    if(history == null){
                        api.sleepSeconds(5);
                        continue;
                    }
                    hasCalculate = history.getIs_get_prize();
                    //hold on thread sleep
                    logger.info("未开奖,睡眠15秒,获取下注历史记录.....");
                    api.sleepSeconds(15);

                }

                //判断输赢
                boolean win = hasCalculate == 1 ? true : false;
                if(win){
                    sumMoney += multiple * 2.99f ;

                    logger.info("恭喜,您上次投注赢啦~");
                    if(failCount == 0){
                        //第一次赢　继续执行　回到初始金钱数
                        multiple = BetConfig.SINGLE.getStartBetMoney();
                    }

                    successCount = successCount + 1 ;
                    if(successCount >= newPlanCount){
                        successCount = 0;
                    }
                    if(successCount == 0){
                        multiple = BetConfig.SINGLE.getStartBetMoney();
                        failCount = 0;
                        lostMoney = 0;
                    }
                }else{

                    sumMoney -= multiple * 7.0f;
                    logger.info("沮丧,您上次投注输啦~");

                    //重算下注金额　
                    failCount++;
                    lostMoney += multiple * 7.0f;


                    //仅仅在输的时候才会去重新算
                    newPlanCount = failCount + 1 ;
                    logger.info("betCountForBalance {}" , newPlanCount);
                    //计算每把要赚多少钱
                    double perEarn = lostMoney / ( newPlanCount * 1.0d );
                    logger.info("perEarn {}" , perEarn);
                    //下次打几倍
                    multiple = (int)Math.ceil(perEarn / 2.9 );
                    logger.info("multiple {}" , multiple);
                }

                logger.info("损失/盈利金额 {} " , sumMoney  );

                logger.info("准备下注  - {}位 1,2,3,4,5,6,7 - {}{}"  , BetConfig.SINGLE.getBetWhere()  , multiple , BetConfig.SINGLE.getBetMoneyUnit());



            }


        } catch (GetBetIssueException e) {
            e.printStackTrace();
        }
    }



    /**
     * 睡眠　单位:秒
     * @param second
     */
    public void sleepSeconds(int second){
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    /**
//     * 主方法
//     * @param args
//     */
//    public static void main(String[] args) throws UsernameNotConfiguredException, PasswdNotConfiguredException, LoginFailedException {
//        Api api = new Api();
//        api.login();
//        logger.info("登陆成功....");
//
//        //投注失败　一直投注
//
//        int failCount = 0;
//
//
//
//        //损失掉的钱
//        int lostMoney = 0;
//
//        //利润
//        float sumMoney = 0;
//        //输一次　就清零
//        int newPlanCount = 0;
//        //只算同一个批次里面的成功个数
//        int successCount = 0;
//
//        int [] betMultipleArr = new int[]{1,3,12,27,57,129};
//        try {
//
//            //永远执行
//
//            int firstBet = betMultipleArr[0];
//
//
//            while(true){
//                try {
//                    Double balance = api.getBalance();
//                    logger.info(" ============= 账户余额 {}" , balance);
//                } catch (GetBalanceException e) {
//                    e.printStackTrace();
//                }
//                boolean betSuccess = false;
//
//
//                String betIssue = null;
//                while (!betSuccess){
//                    //下注
//                    try {
//
//                        IssueData issueAvailable = api.getBetIssue();
//                        String issue = issueAvailable.getCur().getIssue();
//                        logger.info("获取期号成功　{}" , issue);
//
//
//                        //下注万位 1234567位　各1厘
//                        api.bet("cxg360ffc",issue,BetConfig.SINGLE.getBetMoneyUnit(),BetConfig.SINGLE.getBetWhere(),firstBet);
//                        logger.info("下注成功 -  {} - {}位 1,2,3,4,5,6,7 - {}{}" , issue , BetConfig.SINGLE.getBetWhere()  , firstBet , BetConfig.SINGLE.getBetMoneyUnit());
//                        betSuccess = true;
//
//                        betIssue = issue;
//                    } catch (BetFailedException e) {
//                        logger.error(e.getMessage());
//                        try {
//                            logger.info("三秒后再次投注.");
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//
//                try {
//                    Double balance = api.getBalance();
//                    logger.info(" ============= 账户余额 {}" , balance);
//                } catch (GetBalanceException e) {
//                    e.printStackTrace();
//                }
//
//
//                //获取下注历史
//                //先判断时间　未开奖
//                //0 未开奖 1 中奖了　2　未中奖
//                //是否已结算
//                int hasCalculate = 0;
//                while(hasCalculate == 0){
//                    BetHistory history = api.getBetHistoryByIssue(betIssue);
//                    if(history == null){
//                        api.sleepSeconds(5);
//                        continue;
//                    }
//                    hasCalculate = history.getIs_get_prize();
//                    //hold on thread sleep
//                    logger.info("未开奖,睡眠15秒,获取下注历史记录.....");
//                    api.sleepSeconds(15);
//
//                }
//
//                //判断输赢
//                boolean win = hasCalculate == 1 ? true : false;
//                if(win){
//                    sumMoney += firstBet * 2.99f ;
//
//                    logger.info("恭喜,您上次投注赢啦~");
//                    if(failCount == 0){
//                        //第一次赢　继续执行　回到初始金钱数
//                        multiple = BetConfig.SINGLE.getStartBetMoney();
//                    }
//
//                    successCount = successCount + 1 ;
//                    if(successCount >= newPlanCount){
//                        successCount = 0;
//                    }
//                    if(successCount == 0){
//                        multiple = BetConfig.SINGLE.getStartBetMoney();
//                        failCount = 0;
//                        lostMoney = 0;
//                    }
//                }else{
//
//                    sumMoney -= multiple * 7.0f;
//                    logger.info("沮丧,您上次投注输啦~");
//
//                    //重算下注金额　
//                    failCount++;
//                    lostMoney += multiple * 7.0f;
//
//
//                    //仅仅在输的时候才会去重新算
//                    newPlanCount = failCount + 1 ;
//                    logger.info("betCountForBalance {}" , newPlanCount);
//                    //计算每把要赚多少钱
//                    double perEarn = lostMoney / ( newPlanCount * 1.0d );
//                    logger.info("perEarn {}" , perEarn);
//                    //下次打几倍
//                    multiple = (int)Math.ceil(perEarn / 2.9 );
//                    logger.info("multiple {}" , multiple);
//                }
//
//                logger.info("损失/盈利金额 {} " , sumMoney  );
//
//                logger.info("准备下注  - {}位 1,2,3,4,5,6,7 - {}{}"  , BetConfig.SINGLE.getBetWhere()  , multiple , BetConfig.SINGLE.getBetMoneyUnit());
//
//
//
//            }
//
//
//        } catch (GetBetIssueException e) {
//            e.printStackTrace();
//        }
//    }

}
