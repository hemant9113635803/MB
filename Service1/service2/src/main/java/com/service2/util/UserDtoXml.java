package com.service2.util;

import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "UserDtoXml")
@XmlAccessorType (XmlAccessType.FIELD)
public class UserDtoXml implements Serializable {

    @XmlElement(name = "UserDto")
    List<UserDto> userDtos = new ArrayList<>();

    public List<UserDto> getUserDtos() {
        return userDtos;
    }

    public void setUserDtos(List<UserDto> userDtos) {
        this.userDtos = userDtos;
    }
}
