package com.darta.MemberLogin.service;

import com.darta.MemberLogin.model.CustomUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
* 實現官方提供的 UserDetailsService 即可
* */
@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser user = userService.getUserByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("該用戶不存在");
        }
        log.info("用戶名: " + username + "角色: " + user.getAuthorities().toString());
        return user;
    }
}
