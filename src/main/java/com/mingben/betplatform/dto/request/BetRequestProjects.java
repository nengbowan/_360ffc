package com.mingben.betplatform.dto.request;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetRequestProjects {
    private String method_ident;
    private String method_name;
    private String code;
    private String show_code;
    private int num_count;
    private int mode;
    private int multiple;
    private int rebate;
    private String total;
    private List<String> position;
    private List<Double> prize;

    /**
     *
     * @param betMoneyUnit　下注金额单位　元角分厘
     * @param betWhere　　　　下注位　个十百千万
     * @param betMultiple　　　下注倍数　
     * @return
     */
    public static BetRequestProjects generate(String betMoneyUnit , String betWhere,int betMultiple){
        String code = null;
        if("个".equals(betWhere)){
            code = "||||1&2&3&4&5&6&7";
        }else if("十".equals(betWhere)){
            code = "|||1&2&3&4&5&6&7|";
        }else if("百".equals(betWhere)){
            code = "||1&2&3&4&5&6&7||";
        }else if("千".equals(betWhere)){
            code = "|1&2&3&4&5&6&7|||";
        }else if("万".equals(betWhere)){
            code = "1&2&3&4&5&6&7||||";
        }


        double total = 0;
        List<Double> defaultPrize = null;
        int mode = 0;
        if("元".equals(betMoneyUnit)){
            total = betMultiple;
            defaultPrize =  Arrays.asList(new Double[]{ betMultiple * 9.9d});
            mode = 5;
        }else if("角".equals(betMoneyUnit)){
            total = betMultiple / 10d ;
            defaultPrize =  Arrays.asList(new Double[]{ betMultiple * 0.99d});
            mode = 6;
        }else if("分".equals(betMoneyUnit)){
            total = betMultiple / 100d ;
            defaultPrize =  Arrays.asList(new Double[]{ betMultiple * 0.099d});
            mode = 7;
        }else if("厘".equals(betMoneyUnit)){
            total = betMultiple / 1000d ;
            defaultPrize =  Arrays.asList(new Double[]{ betMultiple * 0.0099d});
            mode = 8;
        }

        DecimalFormat format = new DecimalFormat("0.0000");
        String totalStr = format.format(total *  7);




        BetRequestProjects result = BetRequestProjects.builder()
                .method_ident("ssc_n5_dingweidan")
                .method_name("定位胆 - 定位胆")
                .code(code)
                .show_code("1234567,,,,")
                .num_count(7)
                .mode(mode)
                .multiple(betMultiple)
                .rebate(0)
                .total(totalStr)
                .position(null)
                .prize(defaultPrize)
                .build();
        return result;
    }
}