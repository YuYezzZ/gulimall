package com.yuye.gulimall.thirdparty.controller;

import com.yuye.gulimall.thirdparty.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: yuye
 * @Date: 2022/6/21 - 06 - 21 - 22:36
 * @Description: com.yuye.gulimall.thirdparty.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    SmsService smsService;
    @PostMapping("/sendCode")
    public String sendMsg(@RequestParam String phone){
        String s = smsService.sendMsg(phone);
        return s;
    }

    @PostMapping("/check")
    public void check(@RequestParam String phone){
        smsService.checkVerifiyCode(phone,phone);
    }
}
