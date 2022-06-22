package com.yuye.gulimall.auth.feign;

import com.yuye.gulimall.auth.vo.UserRegisterVo;
import com.yuye.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: yuye
 * @Date: 2022/6/22 - 06 - 22 - 20:29
 * @Description: com.yuye.gulimall.auth.feign
 * @version: 1.0
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {
    /**
     * 保存
     */
    @RequestMapping("member/member/save")
    //@RequiresPermissions("member:member:save")
     R save(@RequestBody UserRegisterVo userRegisterVo);
}
