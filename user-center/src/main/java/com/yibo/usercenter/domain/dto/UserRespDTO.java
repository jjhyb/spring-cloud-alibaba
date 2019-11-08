package com.yibo.usercenter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 14:06
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 积分
     */
    private Integer bonus;

    /**
     * 微信昵称
     */
    private String wxNickname;
}
