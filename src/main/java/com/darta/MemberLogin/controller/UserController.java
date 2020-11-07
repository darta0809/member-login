package com.darta.MemberLogin.controller;

import com.darta.MemberLogin.model.CustomUser;
import com.darta.MemberLogin.security.IsAdmin;
import com.darta.MemberLogin.security.IsEditor;
import com.darta.MemberLogin.security.IsReviewer;
import com.darta.MemberLogin.security.IsUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@IsUser // 表明控制器下所有請求都需要登入後才能訪問
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/home")
    public String home(Model model) {
        // 方法一 : 通過 SecurityContextHolder 獲取
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "user/home.html";
    }

    @GetMapping("/editor")
    @IsEditor
    public String editor(Authentication authentication, Model model) {
        // 方法二 : 透過方法注入的形式獲取 Authentication
        CustomUser user = (CustomUser) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user/editor.html";
    }

    @GetMapping("/reviewer")
    @IsReviewer
    public String reviewer(Principal principal, Model model) {
        // 方法三 : 同樣透過方法注入的方法，注意轉型，不推薦使用
        CustomUser user = (CustomUser) ((Authentication)principal).getPrincipal();
        model.addAttribute("user", user);
        return "user/reviewer.html";
    }

    @GetMapping("/admin")
    @IsAdmin
    public String admin(){
        // 方法四 : 透過 Thymeleaf 的 Security 標籤進行，參考 admin.html
        return "user/admin.html";
    }
}
