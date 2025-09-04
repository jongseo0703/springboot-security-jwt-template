package com.example.usertemplate.auth.service;

import com.example.usertemplate.auth.dto.LoginRequest;
import com.example.usertemplate.auth.dto.LoginResponse;
import com.example.usertemplate.auth.dto.RegisterRequest;
import com.example.usertemplate.user.dto.UserResponse;

public interface AuthService {

  UserResponse register(RegisterRequest request);

  LoginResponse login(LoginRequest request);
}
