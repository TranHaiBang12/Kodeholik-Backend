package com.g44.kodeholik.util.validation.file;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = MaxFileSizeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileSize {
    String message() default "MSG60";

    long value(); // Kích thước tối đa tính bằng byte

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
