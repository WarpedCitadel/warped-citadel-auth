package com.warpedcitadel.warpedcitadelauth.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEmailFormat.class)
public @interface EmailFormat {
    String message() default "Invalid password or username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
