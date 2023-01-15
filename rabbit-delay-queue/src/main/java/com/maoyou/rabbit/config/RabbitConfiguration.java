package com.maoyou.rabbit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RabbitConfiguration
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/22 17:44
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableRabbit
public class RabbitConfiguration {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Exchange delayEventExchange() {
        // String name, boolean durable, boolean autoDelete, Map<String, Object> arguments
        TopicExchange topicExchange = new TopicExchange("delay.event.exchange", true, false, null);
        return topicExchange;
    }

    @Bean
    public Queue delayDelayQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments
        Map arguments = new HashMap();
        arguments.put("x-message-ttl", 60 * 1000); // 单位毫秒
        arguments.put("x-dead-letter-exchange", "delay.event.exchange");
        arguments.put("x-dead-letter-routing-key", "delay.business");
        Queue queue = new Queue("delay.delay.queue", true, false, false, arguments);
        return queue;
    }

    @Bean
    public Binding delayDelay() {
        // String destination, DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments
        Binding binding = new Binding("delay.delay.queue",
                Binding.DestinationType.QUEUE,
                "delay.event.exchange",
                "delay.delay",
                null);
        return binding;
    }

    @Bean
    public Queue delayBusinessQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments
        Queue queue = new Queue("delay.business.queue", true, false, false, null);
        return queue;
    }

    @Bean
    public Binding delayBusiness() {
        // String destination, DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments
        Binding binding = new Binding("delay.business.queue",
                Binding.DestinationType.QUEUE,
                "delay.event.exchange",
                "delay.business",
                null);
        return binding;
    }


}
