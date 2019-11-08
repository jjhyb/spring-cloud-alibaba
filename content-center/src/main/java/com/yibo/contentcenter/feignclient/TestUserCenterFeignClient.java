package com.yibo.contentcenter.feignclient;

import com.yibo.contentcenter.domain.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 23:00
 * @Description:
 */

@FeignClient(name="user-center")
public interface TestUserCenterFeignClient {

    @GetMapping("/query")
    UserDTO query(@SpringQueryMap UserDTO userDTO);
}
