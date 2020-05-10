package com.elk.elkkafka.aop;

import com.alibaba.fastjson.JSONObject;
import com.elk.elkkafka.kafka.KafkaSender;
import com.elk.elkkafka.tools.WebToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/8 14:08
 * @Description: AOP拦截日志推送到Kafka
 */
@Aspect
@Slf4j
@Component
public class AopLogAspect {

    @Autowired
    private KafkaSender<JSONObject> kafkaSender;

    // 申明一个切点 里面是 execution表达式
    @Pointcut("execution(* com.elk.*.controller.*.*(..))")
    private void serviceAspect() {
    }

    /**
     * 基于注解形式拦截API请求
     *
     * @param joinPoint
     */
    @Before(value = "@annotation(com.elk.elkkafka.annotation.MonitorRequest)")
    public void doBefore(JoinPoint joinPoint) {
        //获取到请求的属性
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取到请求对象
        HttpServletRequest request = attributes.getRequest();
        //URL：根据请求对象拿到访问的地址
        log.info("url=" + request.getRequestURL());
        //获取请求的方法，是Get还是Post请求
        log.info("method=" + request.getMethod());
        //ip：获取到访问
        log.info("ip=" + request.getRemoteAddr());
        //获取被拦截的类名和方法名
        log.info("class=" + joinPoint.getSignature().getDeclaringTypeName() +
                "and method name=" + joinPoint.getSignature().getName());
        //参数
        log.info("参数=" + joinPoint.getArgs().toString());
    }

    /**
     * 匹配API拦截
     *
     * @param joinPoint
     */
    @Before(value = "serviceAspect()")
    public void methodBefore(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        // 打印请求内容
        log.info("===============请求内容===============");
        log.info("请求地址:" + request.getRequestURL().toString());
        log.info("请求方式:" + request.getMethod());
        log.info("请求类方法:" + joinPoint.getSignature());
        log.info("请求类方法参数:" + Arrays.toString(joinPoint.getArgs()));
        log.info("===============请求内容===============");

        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        jsonObject.put("request_time", df.format(new Date()));
        jsonObject.put("request_url", request.getRequestURL().toString());
        jsonObject.put("request_method", request.getMethod());
        jsonObject.put("signature", joinPoint.getSignature());
        jsonObject.put("request_args", Arrays.toString(joinPoint.getArgs()));
        try {
            jsonObject.put("request_ip", WebToolUtils.getLocalIP());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("request", jsonObject);
        kafkaSender.send(requestJsonObject);
    }

    // 在方法执行完结后打印返回内容
    @AfterReturning(returning = "o", pointcut = "serviceAspect()")
    public void methodAfterReturing(Object o) {

        log.info("--------------返回内容----------------");
        log.info("Response内容:" + o.toString());
        log.info("--------------返回内容----------------");
        JSONObject respJSONObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        jsonObject.put("response_time", df.format(new Date()));
        jsonObject.put("response_content", JSONObject.toJSONString(o));
        respJSONObject.put("response", jsonObject);
        kafkaSender.send(respJSONObject);
    }

}
