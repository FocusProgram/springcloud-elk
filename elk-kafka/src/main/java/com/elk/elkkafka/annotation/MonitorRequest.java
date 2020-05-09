package com.elk.elkkafka.annotation;

import com.elk.elkkafka.AutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * @Target 此注解的作用目标，括号里METHOD的意思说明此注解只能加在方法上面
 * @Retention 注解的保留位置，括号里RUNTIME的意思说明注解可以存在于运行时，可以用于反射
 * @Documented 说明该注解将包含在javadoc中
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MonitorRequest {

}
