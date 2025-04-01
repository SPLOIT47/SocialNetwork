package com.sploit.socialnetwork.auth.annotation;


import com.sploit.socialnetwork.auth.constraint.AtLeastOneNotBlankConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AtLeastOneNotBlankConstraint.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneNotBlank {
    String message() default "";
    String[] fields() default {};
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
}
