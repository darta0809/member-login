package com.darta.MemberLogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class}) // 新版本關閉默認安全訪問控制
public class MemberLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberLoginApplication.class, args);
	}

}
