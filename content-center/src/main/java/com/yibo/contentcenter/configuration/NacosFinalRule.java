package com.yibo.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 19:47
 * @Description:
 */

@Slf4j
public class NacosFinalRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try {
            //负载均衡规则：优先调用同一集群下，符合metadata元数据的实例
            //如果没有，就选择所有集群下，符合metadata的实例

            //1、查询所有实例 A
            //2、筛选元数据匹配的实例 B
            //3、筛选出同cluster下元数据匹配的实例 C
            //4、如果C为空，就用B
            //5、随机选择实例

            String clusterName = nacosDiscoveryProperties.getClusterName();
            String targerVersion = nacosDiscoveryProperties.getMetadata().get("target-version");

            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer)this.getLoadBalancer();
            //想要请求的微服务名称
            String name = loadBalancer.getName();
            //拿到服务发现新的相关的api
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            //所有实例
            List<Instance> instances = namingService.selectInstances(name, true);

            List<Instance> metadataMatchInstances = new ArrayList<>();
            //如果配置了版本映射，那么只调用元数据匹配的实例
            if(!StringUtils.isEmpty(targerVersion)){
                metadataMatchInstances = instances.stream()
                        .filter(instance -> Objects.equals(targerVersion,instance.getMetadata().get("target-version")))
                        .collect(Collectors.toList());
                if(CollectionUtils.isEmpty(metadataMatchInstances)){
                    log.warn("未找到元数据匹配的目标实例！请检查配置，目标元数据配置为：targerVersion={}",targerVersion);
                    return null;
                }
            }

            List<Instance> clusterMetadataMatchInstances = new ArrayList<>();
            //如果配置了集群名称，需筛选同集群下元数据匹配的实例
            if(!StringUtils.isEmpty(clusterName)){
                clusterMetadataMatchInstances = metadataMatchInstances.stream()
                        .filter(instance -> Objects.equals(clusterName,instance.getClusterName()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isEmpty(clusterMetadataMatchInstances)){
                    clusterMetadataMatchInstances = metadataMatchInstances;
                    log.warn("发生跨集群的调用，name={},clusterName={},targerVersion={},instances={}",name,clusterName,targerVersion,instances);
                }
            }

            Instance instance = ExtendBalancer.getHostByRandomWeight2(clusterMetadataMatchInstances);
            log.info("选择的实例是：port={},instance={}",instance.getPort(),instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("发生异常了,e={}",e);
            return null;
        }
    }
}


