package com.yibo.usercenter.rocketmq;

import com.yibo.usercenter.domain.message.UserAddBonusMsgDTO;
import com.yibo.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: huangyibo
 * @Date: 2019/11/7 13:02
 * @Description:
 */

@Component
@RocketMQMessageListener(topic = "add-bonus",consumerGroup = "consumer-group")
@Slf4j
public class AddBonusListener implements RocketMQListener<UserAddBonusMsgDTO> {

    @Autowired
    private UserService userService;


    @Override
    public void onMessage(UserAddBonusMsgDTO message) {
        userService.addBonus(message);
    }
}
