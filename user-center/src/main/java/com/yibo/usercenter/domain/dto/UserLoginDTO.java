package com.yibo.usercenter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 14:11
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    /**
     * 授权码
     */
    private String code;

    /**
     * 微信头像
     */
    private String avatarUrl;

    /**
     * 微信昵称
     */
    private String wxNickname;

}
