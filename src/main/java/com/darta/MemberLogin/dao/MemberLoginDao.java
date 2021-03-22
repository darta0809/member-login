package com.darta.MemberLogin.dao;

import com.darta.MemberLogin.model.CustomUser;
import com.darta.MemberLogin.model.UserAccount;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class MemberLoginDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  // 註冊帳號
  public void createAccount(UserAccount userAccount) {

    log.info(">>>>> Insert ACCOUNT <<<<<");

    String sql = "INSERT INTO ACCOUNT (ID, USERNAME, PASSWORD, EMAIL, STATUS, CODE) "
        + "VALUES (ACCOUNT_SQ.NEXTVAL,?,?,?,?,?)";

    jdbcTemplate.update(sql, userAccount.getUsername(), userAccount.getPassword(), userAccount.getEmail(),
        userAccount.getStatus(), userAccount.getCode());
  }

  // 驗證帳號密碼
  public Boolean validateAccount(UserAccount userAccount) {

    String sql = "SELECT COUNT(*) FROM ACCOUNT "
        + "WHERE USERNAME = ? "
        + "AND PASSWORD = ? ";

    Integer result = jdbcTemplate
        .queryForObject(sql, Integer.class, userAccount.getUsername(), userAccount.getPassword());

    return result != null && result > 0;
  }

  // 修改密碼
  public void updatePassword(UserAccount userAccount) {

    final int expected = 1;
    int actual = 0;

    String sql = "UPDATE ACCOUNT "
        + "SET PASSWORD = ? "
        + "WHERE USERNAME = ? ";

    actual = jdbcTemplate.update(sql, userAccount.getPassword(), userAccount.getUsername());

    if (actual != expected) {
      log.error("ACCOUNT updated rows count should be " + expected + ". actual=" + actual);
      throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, expected, actual);
    }

  }

  // 檢查帳號是否已註冊
  public Boolean checkAccount(String username) {

    String sql = "SELECT COUNT(*) FROM ACCOUNT "
        + "WHERE USERNAME = ? ";

    Integer result = jdbcTemplate.queryForObject(sql, Integer.class, username);

    return result != null && result > 0;
  }

  // 撈取使用者資料
  public List<UserAccount> selectUserInfo(String name, String password) {

    try {

      String sql = "SELECT * FROM ACCOUNT "
          + "WHERE USERNAME = '" + name + "' "
          + "AND PASSWORD = '" + password + "' ";

      return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(UserAccount.class));

    } catch (DataAccessException e) {
      log.error("Load ACCOUNT error. Exception: " + e);
      throw e;
    }
  }

  // 忘記密碼取得 email
  public String getEmail(String user) {

    String sql = "SELECT EMAIL FROM ACCOUNT WHERE USERNAME = '" + user + "' ";

    return jdbcTemplate.queryForObject(sql, String.class);
  }

  // 檢查 email 是否重複
  public Boolean checkEmail(String email) {

    String sql = "SELECT COUNT(*) FROM ACCOUNT WHERE EMAIL = '" + email + "' ";

    Integer result = jdbcTemplate.queryForObject(sql, Integer.class);

    return result != null && result > 0;
  }

  // 檢查帳號 for UserDetailsService loadUserByUsername(String username)
  public CustomUser checkUsername(String username) {

    try {

      String sql = "SELECT * FROM ACCOUNT WHERE USERNAME = '" + username + "'";

      return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(CustomUser.class));

    } catch (DataAccessException e) {
      log.error("Load ACCOUNT error. Exception: " + e);
      throw e;
    }
  }

  /**
   * 開通帳號，修改用戶狀態為 1 進行開通
   *
   * @param userAccount UserAccount
   */
  public void updateUserStatus(UserAccount userAccount) {

    String sql = "UPDATE ACCOUNT "
        + "SET STATUS = ? , CODE = ? "
        + "WHERE USERNAME = ? ";

    jdbcTemplate.update(sql, userAccount.getStatus(), userAccount.getCode(), userAccount.getUsername());
  }

  /**
   * 查詢用戶狀態是否開通
   *
   * @param userAccount UserAccount
   */
  public Boolean loginUser(UserAccount userAccount) {

    String sql = "SELECT STATUS FROM ACCOUNT "
        + "WHERE ID =  '" + userAccount.getId() + "' "
        + "AND USERNAME = '" + userAccount.getUsername() + "' ";

    List<UserAccount> resultList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(UserAccount.class));

    String status = resultList.get(0) == null ? null : resultList.get(0).getStatus();

    return "1".equals(status);
  }

  /**
   * 根據驗證碼去尋找帳戶
   * 用途 : 將該帳戶做開通動作
   */
  public String checkCode(String code) {

    String sql = "SELECT USERNAME FROM ACCOUNT WHERE CODE = '" + code + "'";

    return jdbcTemplate.queryForObject(sql, String.class);
  }
}
