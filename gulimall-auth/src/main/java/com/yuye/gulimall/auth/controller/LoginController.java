package com.yuye.gulimall.auth.controller;

import com.yuye.gulimall.auth.feign.MemberFeignService;
import com.yuye.gulimall.auth.feign.ThirdFeignService;
import com.yuye.gulimall.auth.vo.UserRegisterVo;
import com.yuye.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yuye
 * @Date: 2022/6/22 - 06 - 22 - 20:23
 * @Description: com.yuye.gulimall.auth.controller
 * @version: 1.0
 */
@Controller()
@Slf4j
public class LoginController {
    @Autowired
    ThirdFeignService thirdFeignService;
    @Autowired
    MemberFeignService memberFeignService;
    @GetMapping("/login.html")
    public String login(){

        return "login";
    }

    @PostMapping("/register")
    public String register(@Validated UserRegisterVo userRegisterVo, RedirectAttributes attributes){

        Boolean check = thirdFeignService.check(userRegisterVo.getPhone(), userRegisterVo.getCode());
        if(check){
            R save = memberFeignService.save(userRegisterVo);
            if("0".equals(save.get("code"))) {
                return "redirect:http://auth.gulimall.com/login.html";
            }else {
                log.info("调用member方法异常");
                Map<String, String> errors = new HashMap<>();
                errors.put("msg", "调用member方法异常");
                attributes.addFlashAttribute("errors",errors);
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        }else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code","验证码错误");
            attributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/reg.html";
        }
    }

    @PostMapping("/sms/sendcode")
    public void sendcode(String phone){
        String code = thirdFeignService.sendMsg(phone);
    }
}
