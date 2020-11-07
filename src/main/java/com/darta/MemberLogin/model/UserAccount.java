package com.darta.MemberLogin.model;

import org.springframework.context.annotation.Role;

import lombok.Data;

@Data
public class UserAccount {
	
	public UserAccount() {
	}
	
	public UserAccount(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public UserAccount(String userName, String password, String email) {
		this.userName = userName;
		this.password = password;
		this.email = email;
	}
	
	private String userName;
	private String password;
	private String email;
	
}
