package com.darta.MemberLogin.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.darta.MemberLogin.model.UserAccount;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class MemberLoginDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 註冊帳號
	public void createAccount(UserAccount userAccount) {
		
		log.info(">>>>> Insert ACCOUNT <<<<<");
		
		String sql = "INSERT INTO ACCOUNT (USERNAME, PASSWORD, EMAIL) "
					+ "VALUES (?,?,?)";
		
		jdbcTemplate.update(sql, userAccount.getUserName(), userAccount.getPassword(), userAccount.getEmail());
	}
	
	// 驗證帳號密碼
	public Boolean accountVerity(UserAccount userAccount) {
		
		String sql = "SELECT COUNT(*) FROM ACCOUNT "
					+ "WHERE USERNAME = ? "
					+ "AND PASSWORD = ? ";
		
		Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userAccount.getUserName(), userAccount.getPassword());
		
		return result != null && result > 0;
	}
	
	// 修改密碼
	public void updatePassword(UserAccount userAccount) {
		
		final int expected = 1;
		int actual = 0;
		
		String sql = "UPDATE ACCOUNT "
					+ "SET PASSWORD = ? "
					+ "WHERE USERNAME = ? ";
		
		actual = jdbcTemplate.update(sql, userAccount.getPassword(), userAccount.getUserName());
		
		if (actual != expected) {
			log.error("ACCOUNT updated rows count should be " + expected + ". actual=" + actual);
			throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, expected, actual);
		}

	}
	
	// 檢查是否已註冊
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
			
			List<UserAccount> resultList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(UserAccount.class));
			
			return resultList;
		}catch(DataAccessException e) {
			log.error("Load ACCOUNT error. Exception: " + e);
			throw e;
		}
	}
	
	// 忘記密碼取得 email
	public String getEmail(String user) {
		
		String sql = "SELECT EMAIL FROM ACCOUNT WHERE USERNAME = '" + user +"' ";
		
		String result = jdbcTemplate.queryForObject(sql, String.class);
		
		return result;
	}
	
	// 檢查 email 是否重複
	public Boolean checkEmail(String email) {
		
		String sql = "SELECT COUNT(*) FROM ACCOUNT WHERE EMAIL = '" + email + "' ";
		
		Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
		
		return result != null && result > 0;
	}
	
}
