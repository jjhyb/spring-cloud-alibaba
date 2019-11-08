package com.yibo.contentcenter.feignclient.fallback;

import com.yibo.contentcenter.domain.dto.UserDTO;
import com.yibo.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 15:26
 * @Description:
 */

@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {

    @Override
    public UserDTO findById(Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setWxNickname("一个默认用户");
        return userDTO;
    }
}
