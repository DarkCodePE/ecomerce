package com.example.ecomerce.annotation;

import com.example.ecomerce.validator.MatchPasswordValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchPasswordValidator.class)
@Documented
public @interface MatchPassword {
  String message() default "The new passwords must match";

  Class<?>[] groups() default {};

  boolean allowNull() default false;

  Class<? extends Payload>[] payload() default {};
}
