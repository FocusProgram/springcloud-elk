package com.elk.elkkafka;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/9 15:32
 * @Description:
 */
@EnableAspectJAutoProxy
@ComponentScan({"com.elk.elkkafka"})
public class AutoConfiguration {

}
