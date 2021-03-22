package com.darta.MemberLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {

  @GetMapping("/")
  public String index() {
    return "index/index";
  }

  @GetMapping("/login")
  public String login() {
    return "index/login";
  }

  @GetMapping("/logout")
  public String logout() {
    return "index/logout";
  }

  @GetMapping("/welcome")
  public String welcome() {
    return "welcome.html";
  }
}
