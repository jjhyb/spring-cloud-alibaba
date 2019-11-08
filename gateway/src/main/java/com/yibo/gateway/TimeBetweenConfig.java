package com.yibo.gateway;

import lombok.Data;

import java.time.LocalTime;

/**
 * @author: huangyibo
 * @Date: 2019/11/7 23:06
 * @Description:
 */

@Data
public class TimeBetweenConfig {

    private LocalTime start;

    private LocalTime end;
}
