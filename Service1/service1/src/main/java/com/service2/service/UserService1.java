package com.service2.service;

import com.service2.util.User;

public interface UserService1 {

    public void saveConsumer(User consumer, String fileType);
    public void updateUser(User user, String fileType,String id);
}
