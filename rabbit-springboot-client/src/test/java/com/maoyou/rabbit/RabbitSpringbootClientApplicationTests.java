package com.maoyou.rabbit;

import com.maoyou.rabbit.constant.Constant;
import com.maoyou.rabbit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RabbitSpringbootClientApplicationTests {
    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testSendMessage() {
        User user = new User("maoyou", "maoyou");
        rabbitTemplate.convertAndSend(Constant.EXCHENGE_NAME, Constant.ROUTING_KEY, user);
        log.info("消息发送成功");
    }

    @Test
    void testExchange() {
        Exchange exchange = new DirectExchange(Constant.EXCHENGE_NAME, true, false, null);
        amqpAdmin.declareExchange(exchange);
        log.info("Exchange[{}]创建完成", Constant.EXCHENGE_NAME);
    }

    @Test
    void testQueue() {
        Queue queue = new Queue(Constant.QUEUE_NAME, true, false, false, null);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建完成", Constant.QUEUE_NAME);
    }

    @Test
    void testBinding() {
        Binding binding = new Binding(Constant.QUEUE_NAME, Binding.DestinationType.QUEUE, Constant.EXCHENGE_NAME, Constant.ROUTING_KEY, null);
        amqpAdmin.declareBinding(binding);
        log.info("Binding[{}]创建完成", Constant.ROUTING_KEY);
    }

}
