package com.yuye.gulimall.member;

import com.yuye.gulimall.member.entity.MemberEntity;
import com.yuye.gulimall.member.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: yuye
 * @Date: 2022/5/27 - 05 - 27 - 9:17
 * @Description: com.yuye.gulimall.member
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallMemberApplicationTests {
    @Autowired
    private MemberService memberService;

    @Test
    //测试会员服务插入
    public void insert(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUsername("测试员");
        memberService.save(memberEntity);
        System.out.println("插入成功");
    }
}
