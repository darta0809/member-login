package com.darta.MemberLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class indexController {

    @GetMapping("/")
    public String index(){
        return "index/index";
    }

    @GetMapping("/login")
    public String login(){
        return "index/login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "index/logout";
    }
}
