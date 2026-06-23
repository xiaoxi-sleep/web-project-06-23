package com.courseselection.controller;

import com.courseselection.common.Result;
import com.courseselection.dto.LoginRequest;
import com.courseselection.dto.LoginResponse;
import com.courseselection.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse resp = userService.login(request);
        return Result.success(resp);
    }
}
