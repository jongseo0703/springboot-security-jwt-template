package com.example.usertemplate.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebController {

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/login")
  public String loginPage(
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "logout", required = false) String logout,
      Model model) {
    if (error != null) {
      model.addAttribute("error", "로그인에 실패했습니다.");
    }
    if (logout != null) {
      model.addAttribute("message", "성공적으로 로그아웃되었습니다.");
    }
    return "login";
  }

  @GetMapping("/register")
  public String registerPage() {
    return "register";
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model) {
    return "dashboard";
  }
}
