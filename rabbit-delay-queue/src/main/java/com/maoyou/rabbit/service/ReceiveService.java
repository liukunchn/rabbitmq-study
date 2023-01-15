package com.maoyou.rabbit.service;

import com.maoyou.rabbit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


/**
 * @ClassName ConsumerService
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/22 18:39
 * @Version 1.0
 */
@Service
@Slf4j
public class ReceiveService {

    public void deal(User user) {
        try {
            // 模拟业务处理
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("业务处理时抛出了一个异常");

//        log.info("消息处理完成");
    }
}
