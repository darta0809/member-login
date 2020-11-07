package com.darta.MemberLogin.cofig;

import com.darta.MemberLogin.authentication.AccessDeniedHandlerImpl;
import com.darta.MemberLogin.authentication.AuthenticationFailureHandlerImpl;
import com.darta.MemberLogin.authentication.AuthenticationSuccessHandlerImpl;
import com.darta.MemberLogin.authentication.RestAuthenticationEntryPoint;
import com.darta.MemberLogin.filter.LoginAuthenticationFilter;
import com.darta.MemberLogin.service.CustomUserDetailsService;
import com.darta.MemberLogin.service.MemberLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 開啟方法註釋支持，我們設置 prePostEnabled = true 是為了後面能夠使用 hasRole() 這類表示式
 * 可參考 https://www.baeldung.com/spring-security-method-security
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * TokenBaseRememberMeService 的生成密鑰
     * 算法可參考 https://docs.spring.io/spring-security/site/docs/5.1.3.RELEASE/reference/htmlsingle/#remember-me-hash-token
     */
    private final String SECRET_KEY = "123456";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * 必須有此方法，Spring Security 官方規定必須要有一個加密方式
     * 注意:例如這裡使用了 BCryptPasswordEncoder() 的加密方法，那麼保存用戶密碼時也必須使用這種方法，確保前後一致
     * 參考 Database.java 中保存用戶邏輯
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("aa123").password(new BCryptPasswordEncoder().encode("0000")).roles("ADMIN")
                .and()
                .withUser("user").password(new BCryptPasswordEncoder().encode("0000")).roles("USER");

    }
*/
    /**
     * 配置 Spring Security 注意事項
     * 1. Spring Security 默認開啟了 CSRF，此時我們提交的 POST 表單，必須有隱藏的字段來傳遞 CSRF，而且在 logout 中
     *      必須通過 POST 到 /logout 的方法來退出用戶，詳見 login.html 跟 logout.html
     * 2. 開啟了 rememberMe() 功能後，必須提供 rememberMeServices，例如下面的 getRememberMeService()方法
     *      而且我們只能在 TokenBasedRememberMeService 中設置 cookie 名稱、過期時間等相關配置，如果在別的地方同時配置會報錯
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()    // 登入
                .loginPage("/login")    // 自定義登入頁面
                .failureUrl("/login?error")    // 自定義登入失敗頁面，UI可通過 url 是否有 error 來提供友好的登入提示
                .and()
                .logout()    // 登出
                .logoutUrl("/logout")    // 自定義登出頁面
                .logoutSuccessUrl("/")
                .and()
                .rememberMe()   // 開啟記住密碼功能
                .rememberMeServices(getRememberMeServices())    // 必須提供
                .key(SECRET_KEY)    // 此 SECRET 需要和生成 TokenBasedRememberMeService 的金鑰相同
                .and()
                /*
                * 預設允許所有路徑所有人都可以訪問，確保靜態資源的正常訪問
                * 後面再通過方法註解的方式控制權限
                * */
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403"); // 許可權不足自動跳轉 403
    }

    /**
     * 如果要設置 cookie 過期時間或其他相關配置，在此設定
     */
    private TokenBasedRememberMeServices getRememberMeServices() {
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(SECRET_KEY, customUserDetailsService);
        services.setCookieName("remember-cookie");
        services.setTokenValiditySeconds(100); // 默認 14 天
        return services;
    }

/*
	@Bean
	LoginAuthenticationFilter loginAuthenticationFilter() throws Exception{

		LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());
		filter.setAuthenticationFailureHandler(new AuthenticationFailureHandlerImpl());
		filter.setFilterProcessesUrl("/**");
		return filter;
	}
 */
/*
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
*/
}
