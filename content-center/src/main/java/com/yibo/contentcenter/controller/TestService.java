package com.yibo.contentcenter.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: huangyibo
 * @Date: 2019/11/5 21:52
 * @Description:
 */

@Service
@Slf4j
public class TestService {

    @SentinelResource("common")
    public String common(){
        log.info("common...");
        return "common";
    }
}
