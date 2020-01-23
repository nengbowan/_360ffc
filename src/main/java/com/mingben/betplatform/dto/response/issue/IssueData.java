package com.mingben.betplatform.dto.response.issue;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueData {
    private String lottery_name;
    private String request_time;
    private String opening_issue;
    private IssueCur cur;
    private IssueLast last;
}