package com.darta.MemberLogin.service;

import com.darta.MemberLogin.dao.MemberLoginDao;
import com.darta.MemberLogin.model.CustomUser;
import com.darta.MemberLogin.model.UserAccount;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * 實現官方提供的 UserDetailsService 即可
 * */
@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private MemberLoginService memberLoginService;

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {

    CustomUser user = memberLoginService.checkUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException(username + " 該用戶不存在");
    }

    log.info("用戶名: " + username + "角色: " + user.getAuthorities().toString());

    return user;
  }

}
