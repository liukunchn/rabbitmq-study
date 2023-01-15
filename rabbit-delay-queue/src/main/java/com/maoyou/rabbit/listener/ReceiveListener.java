package com.maoyou.rabbit.listener;

import com.maoyou.rabbit.entity.User;
import com.maoyou.rabbit.service.ReceiveService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


/**
 * @ClassName ConsumerService
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/22 18:39
 * @Version 1.0
 */
@Slf4j
@Service
@RabbitListener(queues = {"delay.business.queue"})
public class ReceiveListener {
    @Autowired
    private ReceiveService receiveService;
    @RabbitHandler
    public void receiveMessage(Message message, Channel channel, User user) throws UnsupportedEncodingException {
        log.info("接收到消息：{}", user);
        log.info("消息属性：{}", message.getMessageProperties());
        log.info("消息体：{}", new String(message.getBody(), "utf-8"));
        receiveService.deal(user);
    }
}
