package com.yibo.contentcenter.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author: huangyibo
 * @Date: 2019/11/7 18:25
 * @Description:
 */
public interface MySource {

    String MY_OUTPUT = "my-output";

    @Output(MY_OUTPUT)
    MessageChannel output();
}
