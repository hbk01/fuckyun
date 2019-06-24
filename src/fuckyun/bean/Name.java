package fuckyun.bean;

import java.lang.annotation.*;

/**
 * 使用此注解为FuckYunObject的字段设置别名
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Name {
    String value();
}
