package com.yibo.contentcenter.service;

import com.yibo.contentcenter.domain.dto.ShareAuditDTO;
import com.yibo.contentcenter.domain.dto.ShareDTO;
import com.yibo.contentcenter.domain.dto.UserDTO;
import com.yibo.contentcenter.domain.entity.RocketmqTransactionLog;
import com.yibo.contentcenter.domain.entity.Share;
import com.yibo.contentcenter.domain.enums.AuditStatusEnum;
import com.yibo.contentcenter.domain.message.UserAddBonusMsgDTO;
import com.yibo.contentcenter.feignclient.UserCenterFeignClient;
import com.yibo.contentcenter.mapper.RocketmqTransactionLogMapper;
import com.yibo.contentcenter.mapper.ShareMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * @author: huangyibo
 * @Date: 2019/11/1 19:11
 * @Description:
 */

@Service
@Slf4j
public class ShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    public ShareDTO findById(Integer id){
        //获取分享详情
        Share share = shareMapper.selectByPrimaryKey(id);
        if(share == null){
            return null;
        }
        //发布人Id
        Integer userId = share.getUserId();

        //调用用户微服务的/user/{userid}?
        UserDTO userDTO = userCenterFeignClient.findById(userId);
        if(userDTO == null){
            return null;
        }
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share,shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }


    public ShareDTO auditById(Integer id, ShareAuditDTO auditDTO){
        //1、查询Share是否存在，不存在或者当前的audit_status != NOT_YET就抛异常
        Share share = shareMapper.selectByPrimaryKey(id);
        if(share == null){
            throw new IllegalArgumentException("参数非法！该分享不存在");
        }
        if(!Objects.equals("NOT_YET",share.getAuditStatus())){
            throw new IllegalArgumentException("参数非法！该分享已审核通过或审核不通过");
        }

        //3、如果是PASS，那么就发送消息到rocketmq，让用户中心去消费，并为发布人添加积分
        //该接口主要为审核，所以加积分使用异步操作，这样可以有效缩短该接口的响应耗时，从而提升用户体验
        if(AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())){
            String transactionId = UUID.randomUUID().toString();
            //发送半消息
            rocketMQTemplate.sendMessageInTransaction(
                    "tx-add-bonus-group",
                    "add-bonus",
                    MessageBuilder.withPayload(UserAddBonusMsgDTO.builder().userId(share.getUserId()).bonus(50).build())
                            //header也有大用处
                            .setHeader(RocketMQHeaders.TRANSACTION_ID,transactionId)
                            .setHeader("share_id",id)
                            .build(),
                    auditDTO);//最后一个参数为args
        }else if(AuditStatusEnum.REJECT.equals(auditDTO.getAuditStatusEnum())){
            this.auditBYIdInDB(id,auditDTO);
        }

        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share,shareDTO);
        return shareDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditBYIdInDB(Integer id,ShareAuditDTO auditDTO) {
        //2、审核资源，将状态设为PASS/REJECT
        Share share = Share.builder().id(id).auditStatus(auditDTO.getAuditStatusEnum().toString())
                .reason(auditDTO.getReason()).build();
        shareMapper.updateByPrimaryKeySelective(share);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditBYIdWithRocketMqLog(Integer id,ShareAuditDTO auditDTO,String transactionId) {
        this.auditBYIdInDB(id,auditDTO);
        RocketmqTransactionLog rocketmqTransactionLog = RocketmqTransactionLog.builder().transactionId(transactionId)
                .log("审核分享...").build();
        rocketmqTransactionLogMapper.insertSelective(rocketmqTransactionLog);
    }
}
