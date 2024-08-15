package com.example.shopit.controller;

import com.example.shopit.dto.request.UserRequest;
import com.example.shopit.dto.response.ResponseDto;
import com.example.shopit.dto.response.UserResponse;
import com.example.shopit.repository.ServicesAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @PostMapping("signup")
    public ResponseDto<UserResponse> saveUser(
            @RequestBody UserRequest userRequest) {
        return ServicesAccessor.getAuthService().signup(userRequest);
    }

    @PostMapping("login")
    public ResponseDto<UserResponse> saveAnnouncement(
            @RequestBody UserRequest userRequest) {
        return ServicesAccessor.getAuthService().login(userRequest);
    }
}
