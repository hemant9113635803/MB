package com.service2.Kafka;

import com.service2.service.UserService;
import com.service2.util.Service2Util;
import com.service2.util.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;

@Service
public class Consumer {

    @Autowired
    Service2Util service2Util;

    @Autowired
    UserService userService;

    @KafkaListener(topics = "userDto",groupId = "mygroup")
    private void consumerFromUserDTO(String data) throws JAXBException {
        String[] dataArray = data.split(":");
        String message = dataArray[0];
        String key = dataArray[1];

        UserDto userDto = (UserDto) service2Util.decryptData(message,key);
        userService.saveConsumer(userDto);

    }

    @KafkaListener(topics = "updateUser",groupId = "mygroup")
    private void updateUser(String data) throws JAXBException {
        String[] dataArray = data.split(":");
        String message = dataArray[0];
        String key = dataArray[1];

        UserDto userDto = (UserDto) service2Util.decryptData(message,key);
        userService.updateUser(userDto);

    }

}
