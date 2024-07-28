package org.example.clientbank.customer.api.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Password must be from 8 to 20 characters long and contain at least 1 uppercase and 1 lowercase letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
