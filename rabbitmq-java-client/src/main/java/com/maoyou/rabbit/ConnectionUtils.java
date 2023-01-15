package com.maoyou.rabbit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 获取连接的工具类
 */
public class ConnectionUtils {
    /**
     * 连接工厂
     */
    private static ConnectionFactory factory;

    /**
     * 静态初始化连接工厂
     */
    static {
        // 创建ConnectionFactory
        factory = new ConnectionFactory();
        // 设置ip、端口、虚拟机、用户名、密码
        factory.setHost("192.168.56.10");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
    }

    /**
     * 获取一个连接
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        return factory.newConnection();
    }

}
