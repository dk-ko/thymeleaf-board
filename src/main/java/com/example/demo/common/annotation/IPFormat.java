package com.example.demo.common.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// TODO 작성해봤는데 동작하지 않아서 우선 보류
@Documented
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = IPFormatValidator.class)
public @interface IPFormat {
    String message() default "Ip format is invalid.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
