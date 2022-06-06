package com.yuye.gulimall.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Auther: yuye
 * @Date: 2022/6/5 - 06 - 05 - 20:22
 * @Description: com.yuye.gulimall.common.volidation
 * @version: 1.0
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {ListValueConstraintValidator.class}
)
public @interface ListValue {
    String message() default "{com.yuye.gulimall.common.valid.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] values() default {};
}
