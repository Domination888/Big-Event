package com.domination.anno;

import com.domination.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented //元注解
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {StateValidation.class}) // 指定提供校验方法的类
public @interface State {
    //提供校验失败信息
    String message() default "state参数只能是已发布或草稿";
    //指定分组
    Class<?>[] groups() default {};
    //负载，获取附加信息
    Class<? extends Payload>[] payload() default {};
}
