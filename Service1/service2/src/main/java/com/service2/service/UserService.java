package com.service2.service;


import com.service2.util.UserDto;
import javax.xml.bind.JAXBException;
import java.util.List;

public interface UserService {

    void saveConsumer(UserDto userDto) throws JAXBException;

    void updateUser(UserDto userDto) throws JAXBException;

    List<UserDto> getUserById(String id, String fileType) throws JAXBException;
}
