package com.example.usertemplate.user.service;

import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.dto.UserUpdateRequest;

/** 유저 로직을 처리하는 UserService임. */
public interface UserService {

  // id로 유저를 가져옴.
  UserResponse getUserById(Long id);

  // 현재 유저의 userId로 갱신 요청을 처리함.
  UserResponse updateCurrentUser(Long userId, UserUpdateRequest request);

  // 현재 유저의 userId로 삭제 요청을 처리함.
  void deleteCurrentUser(Long userId);
}
