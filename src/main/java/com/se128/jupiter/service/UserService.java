package com.se128.jupiter.service;

import com.se128.jupiter.entity.Order;
import com.se128.jupiter.entity.User;

import java.util.List;

public interface UserService {

    User getUserByUserId(Integer userId);

    User getUserByUsernameAndPassword(String username, String password);

    User addUser(User user);

    List<User> getAllUsers();

    User changeUserStatusByUserId(Integer userId);

    User editUser(User user);
}
