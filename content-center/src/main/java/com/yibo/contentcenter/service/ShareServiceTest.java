package com.yibo.contentcenter.service;

import com.yibo.contentcenter.domain.dto.ShareDTO;
import com.yibo.contentcenter.domain.dto.UserDTO;
import com.yibo.contentcenter.domain.entity.Share;
import com.yibo.contentcenter.mapper.ShareMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author: huangyibo
 * @Date: 2019/11/1 19:11
 * @Description:
 */

@Service
@Slf4j
public class ShareServiceTest {

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public ShareDTO findById(Integer id){
        //获取分享详情
        Share share = shareMapper.selectByPrimaryKey(id);
        if(share == null){
            return null;
        }

        //发布人Id
        Integer userId = share.getUserId();

        //获取用户中心所有实例的信息
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        //所有用户中心实例的请求地址
        List<String> targetUrls = instances.stream()
                .map(instance -> instance.getUri().toString() + "/users/{id}")
                .collect(Collectors.toList());
        /*String targetUrl = instances.stream()
                .map(instance -> instance.getUri().toString()+"/users/{id}")
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("当前没有实例"));*/
        //获取集合中随机的下标
        int index = ThreadLocalRandom.current().nextInt(targetUrls.size());
        String targetUrl = targetUrls.get(index);
        log.info("请求的目标地址：{}",targetUrl);
        //调用用户微服务的/user/{userid}?
        UserDTO userDTO = restTemplate.getForObject(targetUrl, UserDTO.class, userId);
        if(userDTO == null){
            return null;
        }
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share,shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }
}
