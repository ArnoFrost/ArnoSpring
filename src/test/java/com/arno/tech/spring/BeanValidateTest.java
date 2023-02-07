package com.arno.tech.spring;

import com.arno.tech.spring.base.utils.ValidatorUtil;
import lombok.Data;
import lombok.ToString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.*;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Person
 *
 * @author xuxin14
 * @date 2023/02/02
 */
@Data
@ToString
class Person {

    @NotNull
    private String name;
    @NotNull
    @Min(0)
    private int age;

    public @NotNull Person getOne(@NotNull @Min(1) Integer id, String name) throws NoSuchMethodException {
        // 校验逻辑
        Method currMethod = this.getClass().getMethod("getOne", Integer.class, String.class);
        Set<ConstraintViolation<Person>> validResult = ValidatorUtil.obtainExecutableValidator().validateParameters(this, currMethod, new Object[]{id, name});
        if (!validResult.isEmpty()) {
            // ... 输出错误详情validResult
            validResult.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
            throw new IllegalArgumentException("参数错误");
        }

        return null;
    }

}

/**
 * 数据类校验测试
 *
 * @author xuxin14
 * @date 2023/02/02
 */
@RunWith(SpringRunner.class)
public class BeanValidateTest {
    public static void main(String[] args) {
    }

    @Test
    public void testBean() {
        Person person = new Person();
        person.setName("xuxin");
        person.setAge(18);
        validateBean(person);
    }

    @Test
    public void testMethod() throws NoSuchMethodException {
        Person person = new Person();
        person.setName("xuxin");
        person.setAge(18);
        person.getOne(1, "xuxin");
        person.getOne(0, "xuxin2");
    }

    /**
     * 打印Bean 的约束信息
     */
    @Test
    public void printBeanConstraintInfo() {
        BeanDescriptor beanDescriptor = obtainValidator().getConstraintsForClass(Person.class);
        System.out.println("此类是否需要校验：" + beanDescriptor.isBeanConstrained());

        // 获取属性、方法、构造器的约束
        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        Set<MethodDescriptor> constrainedMethods = beanDescriptor.getConstrainedMethods(MethodType.GETTER);
        Set<ConstructorDescriptor> constrainedConstructors = beanDescriptor.getConstrainedConstructors();
        System.out.println("需要校验的属性：" + constrainedProperties);
        System.out.println("需要校验的方法：" + constrainedMethods);
        System.out.println("需要校验的构造器：" + constrainedConstructors);

        PropertyDescriptor nameDesc = beanDescriptor.getConstraintsForProperty("name");
        System.out.println(nameDesc);
        System.out.println("name属性的约束注解个数：" + nameDesc.getConstraintDescriptors().size());
    }

    /**
     * 主动校验数据异常
     *
     * @param person
     */
    public static void validateBean(Person person) {
        // 1、使用【默认配置】得到一个校验工厂  这个配置可以来自于provider、SPI提供
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            // 2、得到一个校验器
            Validator validator = validatorFactory.getValidator();
            // 3、校验Java Bean（解析注解） 返回校验结果
            Set<ConstraintViolation<Person>> result = validator.validate(person);

            result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ":" + v.getInvalidValue())
                    .forEach(System.out::println);
        }
    }

    // 用于Java Bean校验的校验器
    private Validator obtainValidator() {
        // 1、使用【默认配置】得到一个校验工厂  这个配置可以来自于provider、SPI提供
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        // 2、得到一个校验器
        return validatorFactory.getValidator();
    }

    // 用于方法校验的校验器
    private ExecutableValidator obtainExecutableValidator() {
        return obtainValidator().forExecutables();
    }
}
