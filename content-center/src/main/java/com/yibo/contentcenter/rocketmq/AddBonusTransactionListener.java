package com.yibo.contentcenter.rocketmq;

import com.yibo.contentcenter.domain.dto.ShareAuditDTO;
import com.yibo.contentcenter.domain.entity.RocketmqTransactionLog;
import com.yibo.contentcenter.mapper.RocketmqTransactionLogMapper;
import com.yibo.contentcenter.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

/**
 * @author: huangyibo
 * @Date: 2019/11/7 14:55
 * @Description:
 */

@Component
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    private ShareService shareService;

    @Autowired
    private RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    /**
     * 发送prepare消息成功此方法被回调，该方法用于执行本地事务
     *
     * @param message 回传的消息，利用transactionId即可获取到该消息的唯一Id
     * @param auditDTO 调用send方法时传递的参数，当send时候若有额外的参数可以传递到send方法中，这里能获取到
     * @return 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：回调
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object auditDTO) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String)headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.parseInt((String)headers.get("share_id"));
        try {
            shareService.auditBYIdWithRocketMqLog(shareId,(ShareAuditDTO)auditDTO,transactionId);
            //本地事物成功，执行commit
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.error("本地事物执行异常，e={}",e);
            //本地事物失败，执行rollback
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 实现本地事务回查的逻辑,并返回本地事务执行状态
     * @param message 通过获取transactionId来判断这条消息的本地事务执行状态
     * @return 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：回调
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String)headers.get(RocketMQHeaders.TRANSACTION_ID);

        RocketmqTransactionLog rocketmqTransactionLog = rocketmqTransactionLogMapper.selectOne(RocketmqTransactionLog
                .builder().transactionId(transactionId).build());
        if(rocketmqTransactionLog == null){
            log.error("如果本地事物日志没有记录，transactionId={}",transactionId);
            //本地事物失败，执行rollback
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        //如果本地事物日志有记录，执行commit
        return RocketMQLocalTransactionState.COMMIT;
    }
}
