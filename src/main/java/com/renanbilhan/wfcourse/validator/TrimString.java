package com.renanbilhan.wfcourse.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.mapstruct.ap.internal.model.common.FinalField;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = { TrimStringValidator.class })
@Target(FIELD)
@Retention(RUNTIME)
public @interface TrimString {

    String message() default "Field must not contain empty spaces in the beggining or the end.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
