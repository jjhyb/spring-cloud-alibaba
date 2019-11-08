package com.yibo.usercenter.configuration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 14:40
 * @Description:
 */

@Configuration
public class WxConfiguration {

    @Bean
    public WxMaConfig wxMaConfig(){
        WxMaDefaultConfigImpl wxMaDefaultConfig = new WxMaDefaultConfigImpl();
        //下面这两个要到微信小程序开发页面去获取
        wxMaDefaultConfig.setAppid("");
        wxMaDefaultConfig.setSecret("");
        return wxMaDefaultConfig;
    }

    @Bean
    public WxMaService wxMaService(){
        WxMaServiceImpl wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(this.wxMaConfig());
        return wxMaService;
    }
}
