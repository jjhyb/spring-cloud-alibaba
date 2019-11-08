package com.yibo.contentcenter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 18:50
 * @Description:
 */

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    /**
     * 待审核
     */
    NOT_YET,

    /**
     * 审核通过
     */
    PASS,

    /**
     * 审核不通过
     */
    REJECT;
}
