package com.yibo.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 23:52
 * @Description:
 */

//feign脱离Ribbon的使用方式
@FeignClient(name = "baidu",url="http://www.baidu.com")
public interface TestBaiduFeignClient {

    @GetMapping("/")
    String index();
}
