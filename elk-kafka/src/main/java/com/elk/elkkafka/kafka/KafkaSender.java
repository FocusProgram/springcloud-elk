package com.elk.elkkafka.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/8 14:17
 * @Description: Kafka推送消息
 */
@Component
@Slf4j
public class KafkaSender<T> {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    /**
     * kafka 发送消息
     *
     * @param obj 消息对象
     */
    public void send(T obj) {
        String jsonObj = JSON.toJSONString(obj);
        log.info("------------ message = {}", jsonObj);

        // 发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(kafkaTopic, jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("Produce: The message failed to be sent:" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                // TODO 业务处理
                log.info("Produce: The message was sent successfully:");
                log.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
            }
        });
    }

}
