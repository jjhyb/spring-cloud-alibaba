package com.yibo.contentcenter.domain.dto;

import com.yibo.contentcenter.domain.enums.AuditStatusEnum;
import lombok.Data;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 18:48
 * @Description:
 */

@Data
public class ShareAuditDTO {

    /**
     * 审核状态
     */
    private AuditStatusEnum auditStatusEnum;

    /**
     * 原因
     */
    private String reason;
}
