package com.service2.controller;

import com.service2.service.UserService1;
import com.service2.util.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    private static Logger log = Logger.getLogger(UserController.class);
    private static final List<String> fileTypeList = Arrays.asList("CSV", "XML", "xml", "csv");

    @Autowired
    UserService1 userService1;


    @PostMapping(path = "/save/{fileType}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveData(@Valid @RequestBody User user,
                                           @PathVariable("fileType") @NotNull(message = "file type is null") String fileType) {
        log.info("Save data started.. in file type: " + fileType);
        try {
            if (!fileType.equalsIgnoreCase("csv") && !fileType.equalsIgnoreCase("xml")) {
                return new ResponseEntity<>("File type should be csv or xml", HttpStatus.OK);
            }
            userService1.saveConsumer(user, fileType);
            return new ResponseEntity<>("Data Saved Successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}/{fileType}")
    public ResponseEntity<String> updateData(@Valid @RequestBody User user, @PathVariable("fileType") @NotNull(message = "file type is null") String fileType,
                                             @PathVariable("id") String id) {

        log.info("Update data started.. in file type: " + fileType);
        try {
            if (!fileTypeList.contains(fileType)) {
                return new ResponseEntity<>("File type should be csv or xml", HttpStatus.OK);
            }
            userService1.updateUser(user, fileType, id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Data Updated Successfully", HttpStatus.OK);
    }


}
