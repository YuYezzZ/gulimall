package com.yuye.gulimall.thirdparty.service;

/**
 * Created by Administrator.
 */
public interface SmsService {

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @return 验证码对应的key
     */
    String sendMsg(String phone);


    /**
     *  校验手机验证码
     * @param verifiyKey 验证码的key
     * @param verifiyCode 验证码
     */
    Boolean checkVerifiyCode(String verifiyKey,String verifiyCode) ;
}
