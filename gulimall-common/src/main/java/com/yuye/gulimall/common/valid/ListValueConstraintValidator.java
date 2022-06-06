package com.yuye.gulimall.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: yuye
 * @Date: 2022/6/6 - 06 - 06 - 15:43
 * @Description: com.yuye.gulimall.common.valid
 * @version: 1.0
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private Set<Integer> set = new HashSet<>();
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        for (int value : values) {
            set.add(value);
        }
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(set.contains(integer))
            return true;
        return false;
    }
}
