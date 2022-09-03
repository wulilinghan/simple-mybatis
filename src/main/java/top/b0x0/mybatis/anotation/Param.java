package top.b0x0.mybatis.anotation;

import java.lang.annotation.*;

/**
 * @author tlh Created By 2022-07-30 23:23
 **/
@Target({ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

    String name() default "";
}
