package com.yibo.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 14:29
 * @Description:
 */

@Slf4j
public class TestControllerBlockHandler {

    /**
     * 处理限流或者降级
     * @param a
     * @param e
     * @return
     */
    public static String block(String a,BlockException e){
        //如果被保护的资源被限流或者降级了，就会抛BlockException
        log.warn("限流，或者降级了...block",e);
        return "限流，或者降级了...block";
    }
}
