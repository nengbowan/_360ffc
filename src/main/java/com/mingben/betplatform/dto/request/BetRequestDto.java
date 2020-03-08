package com.mingben.betplatform.dto.request;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetRequestDto {
    private String client_type;
    private String lottery_ident;
    private String current_issue;
    private double total;
    private List<BetRequestProjects> projects;
    private boolean is_share;
    private int share_rate;

    /**
     *
     * @param issue　期号
     * @param betMoneyUnit　下注金额单位　元角分厘
     * @param betWhere　　　　下注位　个十百千万
     * @param betMultiple　　　下注倍数　
     * @return
     */
    public static BetRequestDto generate(String game, String issue , String betMoneyUnit , String betWhere,int betMultiple){

        BetRequestProjects projects = BetRequestProjects.generate(game , betMoneyUnit , betWhere  , betMultiple);
        BetRequestDto result = BetRequestDto.builder()
                .client_type("WEB")
                .lottery_ident(game)
                .current_issue(issue)
                .total(Double.valueOf(projects.getTotal()))
                .projects(Arrays.asList(projects))
                .is_share(false)
                .share_rate(0)
                .build();
//        System.out.println(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));

        //浏览器请求的
        //{
        //    "client_type":"WEB",
        //    "lottery_ident":"cxg360ffc",
        //    "current_issue":"20200123-1227",
        //    "total":105,
        //    "projects":[
        //        {
        //            "method_ident":"ssc_n5_dingweidan",
        //            "method_name":"定位胆 - 定位胆",
        //            "code":"1&2&3&4&5&6&7||||",
        //            "show_code":"1234567,,,,",
        //            "num_count":7,
        //            "mode":5,
        //            "multiple":15,
        //            "rebate":0,
        //            "total":"105.0000",
        //            "position":[
        //
        //            ],
        //            "prize":[
        //                148.5
        //            ]
        //        }
        //    ],
        //    "is_share":false,
        //    "share_rate":0
        //}

        //self　自己代码fastjson构造的
        //{
        //    "total":105,
        //    "projects":[
        //        {
        //            "mode":5,
        //            "total":"105.0",
        //            "code":"1&2&3&4&5&6&7||||",
        //            "method_ident":"ssc_n5_dingweidan",
        //            "method_name":"定位胆 - 定位胆",
        //            "rebate":0,
        //            "multiple":15,
        //            "num_count":7,
        //            "show_code":"1234567,,,,",
        //            "prize":[
        //                148.5
        //            ]
        //        }
        //    ],
        //    "current_issue":"20200123-1215",
        //    "lottery_ident":"cxg360ffc",
        //    "is_share":false,
        //    "client_type":"WEB",
        //    "share_rate":0
        //}
        return result;


    }
}