package com.maoyou.rabbit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description
 * @Author 刘坤 kunliu@yinhai.com
 * @Date 2022/8/22 17:23
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private String username;
    private String password;
}
