package com.darta.MemberLogin.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@Controller
public class ErrorController {

    @GetMapping("/404")
    public String handle404(){
        return "error/404.html";
    }

    @GetMapping("/403")
    public String handle403(){
        return "error/403.html";
    }

    @GetMapping("/500")
    public String handle500(){
        return "error/500.html";
    }
}
