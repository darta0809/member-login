package com.darta.MemberLogin.cofig;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration implements TransactionManagementConfigurer {

  @Bean
  @ConfigurationProperties("spring.datasource")
  DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(dataSource());
  }

  @Bean
  public PlatformTransactionManager txManager()  {
    return new DataSourceTransactionManager(dataSource());
  }

  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager() {
      return txManager();
  }
}
