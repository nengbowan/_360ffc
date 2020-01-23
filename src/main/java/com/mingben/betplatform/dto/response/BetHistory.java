package com.mingben.betplatform.dto.response;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BetHistory {
    private String no;
    private String issue;
    private int code_status;
    private String projects_code;
    private int multiple;
    private String total_price;
    private String bonus;
    private int is_cancel;
    private int is_get_prize;
    private int prize_status;
    private int share_or_follow_status;
    private String method_name;
    private String issue_code;
    private Date cancel_deadline;
    private Date sale_end;
    private String lottery_name;
    private Date created_at;
    private int allow_cancel;
}
