package com.darta.MemberLogin.controller;

import com.darta.MemberLogin.model.UUIDUtils;
import com.darta.MemberLogin.model.UserAccount;
import com.darta.MemberLogin.service.MemberLoginService;
import com.darta.MemberLogin.service.SendGmailService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

  private final MemberLoginService memberLoginService;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private String getPasswordEncoder(String raw) {
    return passwordEncoder.encode(raw);
  }

  // 檢查帳號是否重複 AJAX
  @PostMapping(value = "/checkUsername")
  @ResponseBody
  public Map<String, Object> checkAccount(@RequestBody Map<String, String> params) {

    String username = params.get("username");

    Boolean result = memberLoginService.checkAccount(username);
    System.out.println("檢查帳號是否重複");
    System.out.println(result);

    Map<String, Object> map = new HashMap<>();

    if (result) {
      map.put("success", true);
    } else {
      map.put("success", false);
    }
    return map;
  }

  // 檢查信箱是否重複 AJAX
  @PostMapping(value = "/checkEmail")
  @ResponseBody
  public Map<String, Object> checkEmail(@RequestBody Map<String, String> params) {

    String email = params.get("email");

    Boolean result = memberLoginService.checkEmail(email);

    System.out.println("檢查信箱是否重複");
    System.out.println(result);

    Map<String, Object> ajaxResult = new HashMap<>();

    ajaxResult.put("success", Boolean.TRUE.equals(result));
    return ajaxResult;
  }

  // 忘記密碼
  @PostMapping(value = "/changePassword")
  public String forgotPassword(ModelMap model, HttpServletRequest request) {

    String userName = request.getParameter("username");

    SendGmailService sendFrom = new SendGmailService("email", "password");
    String email = memberLoginService.getEmail(userName);

    UserAccount user = UserAccount.builder().username(userName).email(email).build();

    sendFrom.passwordResetLink(user);

    model.addAttribute("email", email);

    return "forgot_password.html";
  }

  // 重設密碼
  @PostMapping(value = "/verify")
  public String resetPassword_success(ModelMap model, HttpServletRequest request) {

    String password = getPasswordEncoder(request.getParameter("password"));
    String userName = request.getParameter("username");

    UserAccount user = UserAccount.builder().username(userName).password(password).build();

    memberLoginService.updatePassword(user);

    return "resetPassword_success.html";
  }

  // 註冊帳號
  @PostMapping(value = "/register")
  public String registerSuccess(ModelMap model, HttpServletRequest request) {

    String userName = request.getParameter("username");
    String password = getPasswordEncoder(request.getParameter("password"));
    String email = request.getParameter("email");
    String code = UUIDUtils.getUUID();

    UserAccount addUser = new UserAccount(userName, password, email, code);
    addUser.setStatus("0");

    SendGmailService sendFrom = new SendGmailService("email", "password");
    sendFrom.validationLink(addUser);// 發驗證信
    memberLoginService.createAccount(addUser);// 寫入資料庫

    model.addAttribute("username", userName);
    model.addAttribute("email", email);

    return "register_success.html";
  }

  // 註冊成功，開通帳號
  @GetMapping(value = "/verify")
  public String verify(String code) {

    String username = memberLoginService.checkCode(code);

    System.out.println("code 驗證碼 : " + code);
    // 如果用戶不等於 null，將狀態改為 1 (開通
    if (!username.isEmpty()) {
      UserAccount userAccount = UserAccount.builder().username(username).status("1")
          // 將 code 驗證碼清空
          .code("").build();
      System.out.println(userAccount);
      memberLoginService.updateUserStatus(userAccount);
    }

    return "verify.html";
  }

  // 以下轉頁用

  // 忘記密碼
  @GetMapping(value = "/resetPassword")
  public String resetPassword(String username, Model model) {
    model.addAttribute("username", username);
    return "resetPassword.html";
  }

  // 忘記密碼
  @GetMapping(value = "/forgot")
  public String forgot() {
    return "forgot.html";
  }

  // 註冊會員
  @GetMapping(value = "/toRegister")
  public String toRegister() {
    return "register.html";
  }
}
