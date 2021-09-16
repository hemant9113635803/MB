package com.service2.service;

import com.service2.Kafka.Producer;
import com.service2.util.Service1Util;
import com.service2.util.User;
import com.service2.util.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService1Impl implements UserService1 {
    @Autowired
    Producer producer;

    @Autowired
    UserDto userDto;

    @Autowired
    Service1Util service1Util;

    @Override
    public void saveConsumer(User user, String fileType) {
        getUserDto(user, fileType);
        try {
            Map<String, String> keyStringMap = service1Util.encryptData(userDto);
            keyStringMap.forEach((key, s) -> producer.publicToTopic(s, key));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getUserDto(User user, String fileType) {
        userDto.setName(user.getName());
        userDto.setAge(user.getAge());
        userDto.setDob(user.getDob());
        userDto.setSalary(user.getSalary());
        userDto.setFileType(fileType);
    }

    @Override
    public void updateUser(User user, String fileType, String id) {
        userDto.setId(Integer.parseInt(id));
        getUserDto(user, fileType);
        try {
            Map<String, String> keyStringMap = service1Util.encryptData(userDto);
            keyStringMap.forEach((key, s) -> producer.publicUserUpdateToTopic(s, key));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
