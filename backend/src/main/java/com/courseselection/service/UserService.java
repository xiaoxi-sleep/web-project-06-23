package com.courseselection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.courseselection.dto.LoginRequest;
import com.courseselection.dto.LoginResponse;
import com.courseselection.entity.User;

public interface UserService extends IService<User> {
    LoginResponse login(LoginRequest request);
    User getByUsername(String username);
}
