package com.yibo.usercenter.service;

import com.yibo.usercenter.domain.dto.UserLoginDTO;
import com.yibo.usercenter.domain.entity.BonusEventLog;
import com.yibo.usercenter.domain.entity.User;
import com.yibo.usercenter.domain.message.UserAddBonusMsgDTO;
import com.yibo.usercenter.mapper.BonusEventLogMapper;
import com.yibo.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author: huangyibo
 * @Date: 2019/11/1 18:48
 * @Description:
 */

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BonusEventLogMapper bonusEventLogMapper;

    public User findById(Integer id){

        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        //当收到消息的时候执行的业务
        //1、为用户加积分
        Integer userId = userAddBonusMsgDTO.getUserId();
        Integer bonus = userAddBonusMsgDTO.getBonus();
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateByPrimaryKeySelective(user);
        //2、记录日志到bonus_event_log表里面
        BonusEventLog bonusEventLog = BonusEventLog.builder().userId(userId).value(bonus).event("CONTRIBUTE")
                .createTime(new Date()).description("投稿加积分").build();
        bonusEventLogMapper.insertSelective(bonusEventLog);
        log.info("积分添加完毕。。。。");
    }

    @Transactional(rollbackFor = Exception.class)
    public User login(UserLoginDTO userLoginDTO,String openId){
        User user = this.userMapper.selectOne(User.builder().wxId(openId).build());
        if(user == null){
            User userToSave = User.builder().wxId(openId).bonus(300)
                    .wxNickname(userLoginDTO.getWxNickname())
                    .avatarUrl(userLoginDTO.getAvatarUrl())
                    .roles("user")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            userMapper.insertSelective(userToSave);
            user = userToSave;
        }
        return user;
    }
}
