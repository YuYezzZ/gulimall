package com.yuye.gulimall.search.entity;

import lombok.Data;

/**
 * @Auther: yuye
 * @Date: 2022/6/16 - 06 - 16 - 22:09
 * @Description: com.yuye.gulimall.search.entity
 * @version: 1.0
 */
@Data
public class Account {
    private Long account_number;
    private String address;
    private int age;
    private Long balance;
    private String city;
    private String email;
    private String employer;
    private String firstname;
    private String gender;
    private String lastname;
    private String state;
}
