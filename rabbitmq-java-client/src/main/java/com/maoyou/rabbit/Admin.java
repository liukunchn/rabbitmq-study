package com.maoyou.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Admin
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/19 20:16
 * @Version 1.0
 */
public class Admin {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();

        // 创建管道
        // 参数：channelNumber：要创建的管道的数量
        Channel channel = connection.createChannel(1);

        // 一、声明队列。如果mq服务器中不存在此队列，则创建队列；如果mq服务器中存在此队列，则什么也不做。立即执行到服务器。
        // 参数1：queue ：队列名称
        // 参数2：durable ：是否是持久化队列。如果为true，在mq服务器down掉后，会将队列的数据持久化到erlang内置数据库中。推荐使用true
        // 参数3：exclusive ：是否是专用队列。如果为true，这个队列只能被指定的消费者消费。推荐使用false
        // 参数4：autoDelete ：是否自动删除。如果为true，在队列和所有消费者断开连接后，队列会删除。推荐使用false
        // 参数5：arguments ：队列的其他属性
        AMQP.Queue.DeclareOk queue = channel.queueDeclare(Constant.QUEUE_NAME, true, false, false, null);

        // 二、声明交换机。如果mq服务器中存在此交换机，则什么也不做；如果mq服务器中不存在此交换机，则创建交换机
        //exchange – 交换机名称
        //type – 交换机类型
        //durable – 持久化。如果为true，交换机会在mq服务器重启时恢复
        //autoDelete – 自动删除。如果为true，交换机会在没被使用时自动删除
        //arguments – 交换机的其他属性。
        channel.exchangeDeclare(Constant.EXCHENGE_NAME, BuiltinExchangeType.DIRECT,true, false, null);

        // 三、将队列绑定到交换机
        channel.queueBind(Constant.QUEUE_NAME, Constant.EXCHENGE_NAME, Constant.ROUTING_KEY);

//        Selector selector = Selector.open();
//        SelectableChannel channel1 = null;
//        FileChannel fileChannel = null;
//        fileChannel.read()
//        SelectionKey selectionKey = channel1.register(selector, SelectionKey.OP_ACCEPT);
//        int select = selector.select();
//        while ( int select1 = selector.select();) {
//
//        }

    }
}
