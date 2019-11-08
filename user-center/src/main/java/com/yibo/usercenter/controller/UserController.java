package com.yibo.usercenter.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.yibo.usercenter.auth.CheckLogin;
import com.yibo.usercenter.domain.dto.JwtTokenRespDTO;
import com.yibo.usercenter.domain.dto.LoginRespDTO;
import com.yibo.usercenter.domain.dto.UserLoginDTO;
import com.yibo.usercenter.domain.dto.UserRespDTO;
import com.yibo.usercenter.domain.entity.User;
import com.yibo.usercenter.service.UserService;
import com.yibo.usercenter.util.JwtOperator;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: huangyibo
 * @Date: 2019/11/1 18:51
 * @Description:
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private JwtOperator jwtOperator;

    @GetMapping("/{id}")
    @CheckLogin
    public User findById(@PathVariable("id") Integer id){
        log.info("我被调用了...");
        return userService.findById(id);
    }

    /**
     * 模拟生成token（假的登录）
     * @return
     */
    @GetMapping("/gen-token")
    public String genToken(){
        //直接颁发Token
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("id",1);
        userInfo.put("wxNickname","小明");
        userInfo.put("role","user");
        return jwtOperator.generateToken(userInfo);
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody UserLoginDTO userLoginDTO) throws WxErrorException {
        //微信小程序服务端校验是否已经登录的结果
        WxMaJscode2SessionResult result = wxMaService.getUserService().getSessionInfo(userLoginDTO.getCode());
        //微信的openId，用户在微信这边的唯一标识
        String openid = result.getOpenid();

        //看用户是否注册，如果没有注册就插入数据
        //如果已注册，
        User user = userService.login(userLoginDTO, openid);

        //直接颁发Token
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("id",user.getId());
        userInfo.put("wxNickname",user.getWxNickname());
        userInfo.put("role",user.getRoles());
        String token = jwtOperator.generateToken(userInfo);
        log.info("用户{}登录成功，生成的token={}，有效期：{}",userLoginDTO.getWxNickname(),token,jwtOperator.getExpirationTime());
        //构建响应
        UserRespDTO userRespDTO = new UserRespDTO();
        userRespDTO.setId(user.getId());
        userRespDTO.setBonus(user.getBonus());
        userRespDTO.setAvatarUrl(user.getAvatarUrl());
        userRespDTO.setWxNickname(user.getWxNickname());
        JwtTokenRespDTO jwtTokenRespDTO = new JwtTokenRespDTO();
        jwtTokenRespDTO.setExpirationTime(jwtOperator.getExpirationTime().getTime());
        jwtTokenRespDTO.setToken(token);
        return LoginRespDTO.builder().user(userRespDTO).token(jwtTokenRespDTO).build();
    }
}
