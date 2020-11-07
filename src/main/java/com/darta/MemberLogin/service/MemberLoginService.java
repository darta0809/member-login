package com.darta.MemberLogin.service;

import com.darta.MemberLogin.dao.MemberLoginDao;
import com.darta.MemberLogin.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public class MemberLoginService {
    
    // 註冊帳號
    @Autowired
    private MemberLoginDao memberLoginDao;

    public void createAccount(UserAccount userAccount) {
        memberLoginDao.createAccount(userAccount);
    }

    // 驗證帳號
    public Boolean accountVerity(UserAccount userAccount) {
        return memberLoginDao.accountVerity(userAccount);
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
}
