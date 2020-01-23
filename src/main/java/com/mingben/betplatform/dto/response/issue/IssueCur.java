
package com.mingben.betplatform.dto.response.issue;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueCur {

    private String issue;
    private Date sale_start;
    private Date sale_end;
    private int time_left;
    private int remain_time;

}