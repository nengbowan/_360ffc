
package com.mingben.betplatform.dto.response.issue;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueLast {

    private String issue;
    private List<String> code;

}