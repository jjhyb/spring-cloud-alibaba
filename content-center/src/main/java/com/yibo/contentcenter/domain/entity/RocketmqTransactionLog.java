package com.yibo.contentcenter.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rocketmq_transaction_log")
public class RocketmqTransactionLog {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 事物id
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 日志
     */
    private String log;
}