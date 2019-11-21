package com.yibo.contentcenter.controller;

import com.yibo.contentcenter.auth.CheckLogin;
import com.yibo.contentcenter.domain.dto.ShareDTO;
import com.yibo.contentcenter.domain.vo.ShareVO;
import com.yibo.contentcenter.service.ShareService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: huangyibo
 * @Date: 2019/11/1 19:34
 * @Description:
 */

@RestController
@RequestMapping("/shares")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @GetMapping("/{id}")
    @CheckLogin
    public ShareVO findById(@PathVariable("id") Integer id){
        ShareVO shareVO = new ShareVO();
        ShareDTO shareDTO = shareService.findById(id);
        if(shareDTO != null){
            BeanUtils.copyProperties(shareDTO,shareVO);
        }
        return shareVO;
    }
}
