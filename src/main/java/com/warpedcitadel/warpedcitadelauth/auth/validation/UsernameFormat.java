package com.warpedcitadel.warpedcitadelauth.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUsernameFormat.class)
public @interface UsernameFormat {
    String message() default "Invalid username Format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
