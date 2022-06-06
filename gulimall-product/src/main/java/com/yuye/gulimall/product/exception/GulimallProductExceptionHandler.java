package com.yuye.gulimall.product.exception;

import com.yuye.gulimall.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @Auther: yuye
 * @Date: 2022/6/6 - 06 - 06 - 9:38
 * @Description: com.yuye.gulimall.product.exception
 * @version: 1.0
 */
@RestControllerAdvice(basePackages = "com.yuye.gulimall.product.controller")
public class GulimallProductExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handlerValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach(item->{
            String name = item.getField();
            String message = item.getDefaultMessage();
            map.put("name",name);
            map.put("message",message);
        });
        return R.ok().put("data",map);
    }
}
