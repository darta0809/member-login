package com.darta.MemberLogin.service;

import com.darta.MemberLogin.dao.MemberLoginDao;
import com.darta.MemberLogin.model.CustomUser;
import com.darta.MemberLogin.model.UserAccount;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberLoginService {

  @Autowired
  private MemberLoginDao memberLoginDao;

  // 註冊帳號
  public void createAccount(UserAccount userAccount) {
    memberLoginDao.createAccount(userAccount);
  }

  // 驗證帳號
  public Boolean accountVerity(UserAccount userAccount) {
    return memberLoginDao.validateAccount(userAccount);
  }

  // 修改密碼
  public void updatePassword(UserAccount userAccount) {
    memberLoginDao.updatePassword(userAccount);
  }

  // 檢查帳號密碼
  public Boolean checkAccount(String user) {
    return memberLoginDao.checkAccount(user);
  }

  // 撈出使用者資料
  public List<UserAccount> selectUserInfo(String name, String password) {
    return memberLoginDao.selectUserInfo(name, password);
  }

  // 忘記密碼 GET email
  public String getEmail(String user) {
    return memberLoginDao.getEmail(user);
  }

  // 檢查email有無重複
  public Boolean checkEmail(String email) {
    return memberLoginDao.checkEmail(email);
  }

  // 開通帳號
  public void updateUserStatus(UserAccount userAccount) {
    memberLoginDao.updateUserStatus(userAccount);
  }

  // 用驗證碼去撈取帳戶
  public String checkCode(String code) {
    return memberLoginDao.checkCode(code);
  }

  // 以帳號去取得資訊
  public CustomUser checkUsername(String username) {
    return memberLoginDao.checkUsername(username);
  }
}
