package com.mingben.betplatform.config;

import com.mingben.betplatform.util.PropertiesLoader;
import com.mingben.betplatform.util.StringUtils;
import com.mingben.betplatform.exception.BankCardNameNotConfiguredException;
import com.mingben.betplatform.exception.PasswdNotConfiguredException;
import com.mingben.betplatform.exception.UsernameNotConfiguredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * 下注规则配置类
 */
public class BetConfig {
    private Logger logger = LoggerFactory.getLogger(BetConfig.class);
    private String WEBSITE_KEY = "website";
    private String USERNAME_KEY = "username";
    private String PASSWD_KEY = "passwd";
    private String BETMONEYUNIT_KEY = "betMoneyUnit";
    private String STARTBETMONEY_KEY = "startBetMoney";
    private String BETWHERE_KEY = "betWhere";
    private String BANKCARDNAME_KEY = "bankCardName";


    private static Properties betConfig;

    static {
        try {
            betConfig = PropertiesLoader.loadAllProperties("conf.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BetConfig(){

    }
    //单例模式
    public static  BetConfig SINGLE = new BetConfig();

    public  String getWebSite(){
        return betConfig.getProperty(WEBSITE_KEY,"https://vip.ued01.org");
    }

    public String getUsername()throws UsernameNotConfiguredException{
        String username =  betConfig.getProperty(USERNAME_KEY);
        if(StringUtils.isEmpty(username)){
            throw new UsernameNotConfiguredException();
        }
        return username;
    }

    public String getPasswd()throws PasswdNotConfiguredException{
        String passwd =  betConfig.getProperty(PASSWD_KEY);
        if(StringUtils.isEmpty(passwd)){
            throw new PasswdNotConfiguredException();
        }
        return passwd;
    }

    /**
     * 圆角分厘
      * @return if not value set , 厘 is the default value
     */
    public String getBetMoneyUnit(){
        String betMoneyUnit =  betConfig.getProperty(BETMONEYUNIT_KEY,"厘");
        return betMoneyUnit;
    }

    /**
     *
     * @return if not value set , 1 is the default value
     */
    public Integer getStartBetMoney(){
        String betMoneyUnit =  betConfig.getProperty(STARTBETMONEY_KEY,"1");
        return Integer.valueOf(betMoneyUnit);
    }

    /**
     * 个十百千万
     * @return if not value set , 万 is the default value
     */
    public String getBetWhere(){
        String betWhere =  betConfig.getProperty(BETWHERE_KEY,"万");
        return betWhere;
    }

    /**
     * 获取银行卡名称
     * @return if not value set , 万 is the default value
     */
    public String getBankCardName()throws BankCardNameNotConfiguredException{
        String bankCardName =  betConfig.getProperty(BANKCARDNAME_KEY);
        if(StringUtils.isEmpty(bankCardName)){
            throw new BankCardNameNotConfiguredException();
        }
        return bankCardName;
    }





}
