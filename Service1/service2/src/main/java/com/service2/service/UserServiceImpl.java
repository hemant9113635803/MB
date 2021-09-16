package com.service2.service;

import com.service2.util.Service2Util;
import com.service2.util.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    Service2Util service2Util;

    @Override
    public void saveConsumer(UserDto userDto) throws JAXBException {
        if (userDto.getFileType().equalsIgnoreCase("CSV")) {
            service2Util.writeIntoCSV(userDto);
        } else if (userDto.getFileType().equalsIgnoreCase("xml")) {
            service2Util.writeIntoXMLFile(userDto);
        }
        System.out.println("<Write operation Done> " + "File Type: " + userDto.getFileType());
    }

    @Override
    public void updateUser(UserDto userDto) throws JAXBException {
        if (userDto.getFileType().equalsIgnoreCase("CSV")) {
            service2Util.updateUserCSV(userDto);
        } else if (userDto.getFileType().equalsIgnoreCase("xml")) {
            service2Util.updateXMLFile(userDto);
        }
        System.out.println("<Update operation Done> " + "File Type: " + userDto.getFileType());
    }

    @Override
    public List<UserDto> getUserById(String id, String fileType) throws JAXBException {
        return service2Util.getDataById(id,fileType);
    }
}
