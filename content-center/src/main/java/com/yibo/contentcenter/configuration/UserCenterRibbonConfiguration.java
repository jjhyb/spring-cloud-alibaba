package com.yibo.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 18:05
 * @Description:
 *
 * @RibbonClient(name="user-center",configuration = RibbonConfiguration.class)
 *  指定user-center实例的ribbon负载均衡策略为RandomRule
 *
 *  @RibbonClients(defaultConfiguration = RibbonConfiguration.class) 实现Ribbon负载均衡的全局配置
 *
 */

//下面两行代码为用java代码的方式配置ribbon的负载均衡策略
@Configuration
//@RibbonClient(name="user-center",configuration = RibbonConfiguration.class)
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)//Ribbon负载均衡的全局配置
public class UserCenterRibbonConfiguration {
}
