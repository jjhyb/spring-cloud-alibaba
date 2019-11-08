package com.yibo.contentcenter.feignclient;

import com.yibo.contentcenter.domain.dto.UserDTO;
import com.yibo.contentcenter.feignclient.fallbackFactory.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 21:45
 * @Description:
 */
//java代码的配置方式
//@FeignClient(name="user-center",configuration = UserCenterFeignConfiguration.class)
@FeignClient(name="user-center",
            //fallback和fallbackFactory只能使用一个，fallbackFactory比fallback功能强大
//        fallback = UserCenterFeignClientFallback.class,
        fallbackFactory = UserCenterFeignClientFallbackFactory.class)
public interface UserCenterFeignClient {

    /**
     * http://user-center/users/{id}
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable("id") Integer id);
}
