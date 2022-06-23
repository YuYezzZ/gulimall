package com.yuye.gulimall.auth.controller;

import com.yuye.gulimall.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * @Auther: yuye
 * @Date: 2022/6/22 - 06 - 22 - 20:23
 * @Description: com.yuye.gulimall.auth.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("/auth")
@Slf4j
public class Auth2Controller {
    @GetMapping("/login")
    public void authLogin(String code) throws Exception {
        log.info("code:{}",code);
        String url = "https://github.com/login/oauth/access_token";
        HashMap<String, String> param = new HashMap<>();
        param.put("client_secret","bebab41c703eaa29d9b3dd1b9ae0dc20eb041980");
        param.put("client_id","0a2785e6b175c0aa0e80");
        param.put("code",code);
        HttpResponse response = HttpUtils.doPost("https://github.com", "/login/oauth/access_token", "post", new HashMap<>(), param, new HashMap<>());
        log.info("response:{}",response);

        if(response.getStatusLine().getStatusCode()==200){
            String json = EntityUtils.toString(response.getEntity());
            log.info("json字符串：{}",json);
            String[] split = json.split("=");
            String token= split[1].split("&")[0];
            HashMap<String, String> map = new HashMap<>();
            map.put("Authorization","Bearer "+token);
            HttpResponse result = HttpUtils.doGet("https://api.github.com", "/user", "get", map, new HashMap<>());
            if(result.getStatusLine().getStatusCode()==200){
                String s = EntityUtils.toString(result.getEntity());
                log.info("获取用户信息：{}",s);
            }
            log.info("response:{}",response);
        }
    }
}
