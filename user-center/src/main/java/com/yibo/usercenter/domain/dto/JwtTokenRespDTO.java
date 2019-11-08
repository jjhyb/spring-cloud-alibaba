package com.yibo.usercenter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 13:51
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenRespDTO {

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Long expirationTime;
}
