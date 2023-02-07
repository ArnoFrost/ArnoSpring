package com.arno.tech.spring.base.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.util.Set;

/**
 * 数据校验工具类
 *
 * @author xuxin14
 * @date 2023/02/02
 */
public abstract class ValidatorUtil {

    public static ValidatorFactory obtainValidatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }

    public static Validator obtainValidator() {
        return obtainValidatorFactory().getValidator();
    }

    public static ExecutableValidator obtainExecutableValidator() {
        return obtainValidator().forExecutables();
    }

    public static <T> void printViolations(Set<ConstraintViolation<T>> violations) {
        violations.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
    }

}