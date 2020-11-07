package com.darta.MemberLogin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.darta.MemberLogin.model.UserAccount;
import com.darta.MemberLogin.service.MemberLoginService;
import com.darta.MemberLogin.service.SendGmailService;

//@Controller
public class LoginController {

	@Autowired
	private MemberLoginService memberLoginService;

	@PostMapping(value = "/index")
	public String login(ModelMap model, HttpServletRequest request) {

		String password = request.getParameter("password");
		String userName = request.getParameter("username");
		String email = request.getParameter("email");

		password = DigestUtils.md5DigestAsHex(password.getBytes());

		UserAccount userAccount = new UserAccount();
		userAccount.setUserName(userName);
		userAccount.setPassword(password);
		userAccount.setEmail(email);

		Boolean result = memberLoginService.accountVerity(userAccount);

		if (result == true) {
			List<UserAccount> resultList = memberLoginService.selectUserInfo(userName, password);
			model.addAttribute("username", userName);
			model.addAttribute("email", resultList.get(0).getEmail());
			return "welcome.html";
		} else {
			return "login_fail.html";
		}
	}

	@PostMapping(value = "/checkusername")
	@ResponseBody
	public Map<String, Object> checkAccount(@RequestBody Map<String, String> params) {

		String userName = params.get("username");

		// 檢查帳號是否重複 AJAX
		Boolean b = memberLoginService.checkAccount(userName);
		System.out.println("檢查帳號是否重複");
		System.out.println(b);

		Map<String, Object> map = new HashMap<>();

		if (b) {
			map.put("success", true);
			return map;
		} else {
			map.put("success", false);
			return map;
		}
	}

	@PostMapping(value = "/checkemail")
	@ResponseBody
	public Map<String, Object> checkemail(@RequestBody Map<String, String> params) {

		String email = params.get("email");

		Boolean b = memberLoginService.checkEmail(email);

		System.out.println("檢查信箱是否重複");
		System.out.println(b);

		Map<String, Object> map = new HashMap<>();

		if (b) {
			map.put("success", true);
			return map;
		} else {
			map.put("success", false);
			return map;
		}
	}

	@PostMapping(value = "/changepassword")
	public String forgotPassword(ModelMap model, HttpServletRequest request) throws Exception {

		String userName = request.getParameter("username");

		// 忘記密碼
		SendGmailService sendfrom = new SendGmailService("darta0809@gmail.com", "shit5205");
		String email = memberLoginService.getEmail(userName);

		UserAccount user = new UserAccount();
		user.setUserName(userName);
		user.setEmail(email);

		sendfrom.passwordResetLink(user);

		model.addAttribute("email", email);

		return "forgot_password.html";
	}

	@PostMapping(value = "/verify")
	public String resetPassword_success(ModelMap model, HttpServletRequest request) throws Exception {

		String password = request.getParameter("password");
		String userName = request.getParameter("username");
		String email = request.getParameter("email");

		// 重設密碼
		UserAccount user1 = new UserAccount();
		password = DigestUtils.md5DigestAsHex(password.getBytes()); // 轉MD5
		user1.setUserName(userName);
		user1.setEmail(email);
		user1.setPassword(password);

		memberLoginService.updatePassword(user1);

		return "resetPassword_success.html";
	}

	@PostMapping(value = "/register")
	public String registerSuccess(ModelMap model, HttpServletRequest request) throws Exception {

		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		// 註冊帳號
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		UserAccount addUser = new UserAccount(userName, password, email);
		SendGmailService sendfrom = new SendGmailService("darta0809@gmail.com", "shit5205");
		sendfrom.validationLink(addUser);// 發驗證信
		memberLoginService.createAccount(addUser);// 寫入資料庫

		model.addAttribute("username", userName);
		model.addAttribute("email", email);

		return "register_success.html";
	}

	// 以下轉頁用

	// 忘記密碼
	@GetMapping(value = "/resetpassword")
	public String resetPassword() {
		return "resetPassword.html";
	}

	// 登入首頁
	@GetMapping(value = "/login")
	public String homepage() {
		return "index.html";
	}

	// 忘記密碼
	@GetMapping(value = "/forgot")
	public String forgot() {
		return "forgot.html";
	}

	// 註冊會員
	@GetMapping(value = "/toregister")
	public String toregister() {
		return "register.html";
	}

	// 403
	@RequestMapping("/403")
	public String forbidden() {
		return "403";
	}
}
