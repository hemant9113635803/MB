package com.service2.controller;

import com.service2.service.UserService;
import com.service2.util.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/read/{id}/{fileType}")
    public ResponseEntity<List<UserDto>> readData(@PathVariable("id") String id, @PathVariable("fileType") String fileType) throws JAXBException {
        List<UserDto> list =  userService.getUserById(id,fileType);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
