package com.yibo.usercenter.rocketmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: huangyibo
 * @Date: 2019/11/7 18:38
 * @Description:
 */
public interface MySink {

    String MY_INPUT = "my-input";

    @Input(MY_INPUT)
    SubscribableChannel input();
}
