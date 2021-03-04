package com.darta.MemberLogin.model;

import java.util.ArrayList;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUser extends UserAccount implements UserDetails {

  private static final long serialVersionUID = 1L;

  public CustomUser() {
  }

  public CustomUser(UserAccount user) {
    if (user != null) {
      this.setUsername(user.getUsername());
      this.setPassword(user.getPassword());
      this.setEmail(user.getEmail());
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    String username = this.getUsername();
    if (username != null) {
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
      authorities.add(authority);
    }
    return authorities;
  }

  // 帳戶是否未過期，過期無法驗證
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // 指定用戶是否解鎖，鎖定的用戶無法進行身分驗證
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // 指示是否已過期的用戶憑據(密碼)，過期的憑據防止認證
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // 是否可用，禁用的用戶不能身分驗證
  @Override
  public boolean isEnabled() {
    return true;
  }
}
