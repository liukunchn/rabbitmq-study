package com.maoyou.rabbit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @ClassName RabbitConfiguration
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/22 17:44
 * @Version 1.0
 */
@Configuration
@Slf4j
public class RabbitTemplateConfiguration {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void initRabbitTemplate() {
        // 消息从生产者到mq服务器Broker成功时回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    log.info("消息{}已送达服务器。correlationData={},ack={},caouse={}", correlationData.getId(), correlationData, ack, cause);
                } else {
                    log.info("消息{}未送达服务器。correlationData={},ack={},caouse={}", correlationData.getId(), correlationData, ack, cause);
                }
                // 如果回调没有发生，那么应该也是当作消息未送达
            }
        });
        // 消息从交换机到队列失败时回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                log.info("消息{}未送达队列。returned={}", returned.getMessage().getMessageProperties().getHeader("spring_returned_message_correlation"), returned);
            }
        });
    }
}
