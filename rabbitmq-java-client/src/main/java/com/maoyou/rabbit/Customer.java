package com.maoyou.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Customer
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/19 19:50
 * @Version 1.0
 */
public class Customer {


    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();

        // 创建管道
        // 参数：channelNumber：要创建的管道的数量
        Channel channel = connection.createChannel(1);

        // 一、创建消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /**
             * 三、接收消息
             * @param consumerTag 消费者标签
             * @param envelope 信封（交付标签、是否重新交付、交换机、路由键）
             * @param properties 消息属性（文本类型、文本编码、头信息、交付模式、消息id等）
             * @param body 消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumerTag:" + consumerTag);
                System.out.println("envelope:" + envelope);
                System.out.println("properties:" + properties);
                System.out.println("body:" + new String(body));

                try {
                    Thread.sleep(30*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 确认消息
                // 参数1：deliveryTag：交付标签
                // 参数2：multiple：是否确认交付标签之前的多个消息
//                channel.basicAck(envelope.getDeliveryTag(), false);

                // 拒绝消息
                // 参数1：deliveryTag：交付标签
                // 参数2：requeue：是否重新进入队列
                //                  如果为true，被拒绝的消息重新进入队列排队（通常会立刻再次发送此消息给消费者）
                //                  如果为false，且此队列没有配置死信交换机，被拒绝的消息将被丢弃
                //                  如果为false，且此队列配置了死信交换机，被拒绝的消息将被路由到死信交换机
//                channel.basicReject(envelope.getDeliveryTag(), true);


            }
        };

        // 三，消费者流量设置。（注意：这个设置要写在消费者监听队列之前，否则消费者无法应用这个设置）
        // 参数1：prefetchSize：mq服务器在接收到ack之前，可以交付的最大容量（单位为8B），默认为0表示不收限制
        // 参数2：prefetchCount：mq服务器在接收到ack之前，可以交付的最大消息数量，默认为0表示不受限制
        // 参数3：global：将设置应用于全部管道而不是这个消费者？默认为false
        // todo 一定注意：prefetchCount配置的mq服务器在接收到ack之前可以交付的最大消息数量，和consumer或者listener没有关系,consumer或者Listener只能一个一个地处理
        channel.basicQos(1);// 表示在服务器接收到ack之前，不会再发送消息给消费者

        // 二、消费者监听队列。当队列发生变化，会回调消费者的handleDelivery()方法
        // 参数1：queue:队列名称
        // 参数2：autoAck:自动确认
        // 参数3：callback：消费者
        channel.basicConsume(Constant.QUEUE_NAME, true, consumer);




    }
}
