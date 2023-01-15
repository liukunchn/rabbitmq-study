package com.maoyou.rabbit.service;

import com.maoyou.rabbit.constant.Constant;
import com.maoyou.rabbit.entity.User;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


/**
 * @ClassName ConsumerService
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/22 18:39
 * @Version 1.0
 */
@Component
@RabbitListener(queues = {Constant.QUEUE_NAME})
@Slf4j
public class ReceiveService {
    @RabbitHandler
    public void receiveMessage(Message message, Channel channel, User content) throws UnsupportedEncodingException {
        log.info("接收到消息：{}", content);
        try {
            // 模拟业务处理
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("消息属性：{}", message.getMessageProperties());
        log.info("消息体：{}", new String(message.getBody(), "utf-8"));
        throw new RuntimeException("模拟业务异常");
//        log.info("消息处理完成");
    }
}
