package com.darta.MemberLogin.model;

import lombok.Data;

@Data
public class UserAccount {

  private String id;
  private String username;
  private String password;
  private String email;
  private String status; // 0 代表未開通， 1 代表開通
  /*
    利用 UUID 生成一段數字，發送到用戶信箱，當用戶點擊連結時
    做一個檢核，如果用戶傳來的 code 符合時，更改狀態為 1 來開通用戶
  */
  private String code;

  public UserAccount() {
  }

  public UserAccount(String userName, String password) {
    this.username = userName;
    this.password = password;
  }

  public UserAccount(String userName, String password, String email, String code) {
    this.username = userName;
    this.password = password;
    this.email = email;
    this.code = code;
  }

}
