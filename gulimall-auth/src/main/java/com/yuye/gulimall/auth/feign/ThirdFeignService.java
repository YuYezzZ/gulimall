package com.yuye.gulimall.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: yuye
 * @Date: 2022/6/22 - 06 - 22 - 22:31
 * @Description: com.yuye.gulimall.auth.feign
 * @version: 1.0
 */
@FeignClient("gulimall-thridparty")
public interface ThirdFeignService {
    @PostMapping("/thirdparty/sms/sendCode")
    String sendMsg(@RequestParam String phone);

    @PostMapping("/check")
    Boolean check(@RequestParam("phone") String phone,@RequestParam("code") String code);
}
