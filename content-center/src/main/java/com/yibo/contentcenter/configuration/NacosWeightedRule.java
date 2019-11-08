package com.yibo.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 18:44
 * @Description: 继承AbstractLoadBalancerRule编写负载均衡算法，支持Nacos的权重
 */

@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    /**
     * 读取配置文件并初始化
     * @param iClientConfig
     */
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }


    @Override
    public Server choose(Object o) {
        try {
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer)this.getLoadBalancer();
            log.info("loadBalancer={}",loadBalancer);
            //想要请求的微服务名称
            String name = loadBalancer.getName();
            //实现基于权重的负载均衡算法
            //拿到服务发现新的相关的api
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            //Nacos client自动通过基于权重的负载均衡算法，给我们选择一个实例
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("Nacos client选择的实例：port={} , instance={}",instance.getPort(),instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("Nacos client自动通过基于权重的负载均衡算法，选择微服务实例异常,e={}",e);
            return null;
        }
    }
}

//spring cloud commons ---> 定义了标准
//spring cloud loadbalancer ---> 没有权重