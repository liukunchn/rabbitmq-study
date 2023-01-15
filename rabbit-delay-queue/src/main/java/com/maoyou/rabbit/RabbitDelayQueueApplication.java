package com.maoyou.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 延时队列
 *  1.延时队列原理
 *      1.1 消息的ttl
 *          消息的ttl就是消息的存活时间
 *          可以给Message设置ttl：通过给Message添加expiration属性实现
 *          todo 也可以给Queue设置ttl：通过给Queue添加x-message-ttl属性实现
 *          如果给Message设置ttl，rabbitmq会使用惰性检查，导致延时消息不按预期的顺序过期，所以一般使用给Queue设置ttl的方式
 *      1.2 死信
 *          一个Message在满足如下条件下会变成死信
 *              消息被消费者拒收，并且指定requeue=false
 *              消息过期
 *              消息队列溢出
 *      1.3 死信交换机，死信路由键
 *          如果一个Queue指定了死信交换机和死信路由键，那么这个Queue的Message在变成死信后会进入死信交换机
 *          todo 给Queue指定死信交换机：x-dead-letter-exchange
 *          todo 给Queue指定死信路由键：x-dead-letter-routing-key
 *      1.4 延时队列
 *          给一个Queue指定ttl，死信交换机，死信路由键，这个Queue即使延时队列
 *  2.延时队列的实现
 *      2.1 使用两个交换机
 *          交换机：delay.delay.exchange -> 路由键：delay.delay -> 延时队列：delay.delay.queue ->
 *          死信交换机：delay.business.exchange -> 死信路由键：delay.business -> 业务队列：delay.business.queue
 *      2.2 todo 只用一个交换机
 *          交换机：delay.event.exchange -> 路由键：delay.delay -> 延时队列：delay.delay.queue
 *          死信交换机：delay.event.exchange -> 死信路由键：delay.business -> 业务队列：delay.business.queue
 *  3.在springboot项目中，声明Exchange,Queue,Binding可以直接向容器中注入bean实例
 *      todo 但是，需要配置消费者监听才会去服务器创建队列，交换机，绑定
 *
 */
@SpringBootApplication
public class RabbitDelayQueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitDelayQueueApplication.class, args);
    }

}
