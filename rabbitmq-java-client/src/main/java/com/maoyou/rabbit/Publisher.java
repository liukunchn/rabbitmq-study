package com.maoyou.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Test
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/19 19:46
 * @Version 1.0
 */
public class Publisher {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();

        // 创建管道
        // 参数：channelNumber：要创建的管道的数量
        Channel channel = connection.createChannel(1);

        // 一、发送消息
        // 参数1：exchange  ：交换机。如果使用简单队列模式或工作队列模式，交换机的值为空串""
        // 参数2：routingKey  ：队列或路由键。如果使用简单队列模式或工作队列模式，这个参数表示队列；否则，表示路由键
        // 参数3：props  ：消息的其他属性。路由头信息等
        // 参数4：body  ：表示消息内容的字节数组。
        channel.basicPublish(Constant.EXCHENGE_NAME, Constant.ROUTING_KEY, null, "Hello World!".getBytes());

        System.out.println("消息发送完成");
        connection.close();

    }
}
