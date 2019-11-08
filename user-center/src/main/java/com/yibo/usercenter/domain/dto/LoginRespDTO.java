package com.yibo.usercenter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 14:08
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRespDTO {

    /**
     * token
     */
    private JwtTokenRespDTO token;

    /**
     * 用户信息
     */
    private UserRespDTO user;
}
