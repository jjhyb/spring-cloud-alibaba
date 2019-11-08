package com.yibo.contentcenter.feignclient.fallbackFactory;

import com.yibo.contentcenter.domain.dto.UserDTO;
import com.yibo.contentcenter.feignclient.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 15:43
 * @Description:
 */

@Component
@Slf4j
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {

    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        return new UserCenterFeignClient(){
            @Override
            public UserDTO findById(Integer id) {
                log.warn("远程调用被限流/降级了,throwable={}",throwable);
                UserDTO userDTO = new UserDTO();
                userDTO.setWxNickname("一个默认用户");
                return userDTO;
            }
        };
    }
}
