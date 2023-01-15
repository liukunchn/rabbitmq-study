package com.maoyou.rabbit.controller;

import com.maoyou.rabbit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @ClassName SendController
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/22 18:52
 * @Version 1.0
 */
@Controller
@Slf4j
public class SendController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @ResponseBody
    @RequestMapping("/sendMessage")
    public String sendMessage() {
        User user = new User("maoyou", "maoyou");
        rabbitTemplate.convertAndSend("delay.event.exchange", "delay.delay", user, new CorrelationData(UUID.randomUUID().toString().replaceAll("-", "")));
        return "消息已发送";
    }
}
