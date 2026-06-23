package com.courseselection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.courseselection.dto.LoginRequest;
import com.courseselection.dto.LoginResponse;
import com.courseselection.entity.User;
import com.courseselection.mapper.UserMapper;
import com.courseselection.security.JwtUtil;
import com.courseselection.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = getByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("账户已被禁用");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        LoginResponse resp = new LoginResponse();
        resp.setToken(jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole()));
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRole(user.getRole());
        resp.setName(user.getName());
        return resp;
    }

    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }
}
