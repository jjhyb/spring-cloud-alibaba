package com.yibo.contentcenter.controller;

import com.yibo.contentcenter.auth.CheckAuthorization;
import com.yibo.contentcenter.domain.dto.ShareAuditDTO;
import com.yibo.contentcenter.domain.dto.ShareDTO;
import com.yibo.contentcenter.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 18:46
 * @Description:
 */

@RestController
@RequestMapping("/admin/shares")
public class ShareAdminController {

    @Autowired
    private ShareService shareService;

    @PutMapping("/audit/{id}")
    @CheckAuthorization("admin")
    public ShareDTO auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO auditDTO){

        return shareService.auditById(id,auditDTO);
    }
}
