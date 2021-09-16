package com.service2.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    public static final String userDto = "userDto";
    public static final String UPDATE_USER_DATA = "updateUser";

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void publicToTopic(String message, String key){
        String producer = message+":"+key;
        this.kafkaTemplate.send(userDto, producer);

    }
    public void publicUserUpdateToTopic(String message, String key){
        String producer = message+":"+key;
        this.kafkaTemplate.send(UPDATE_USER_DATA, producer);
    }
}
