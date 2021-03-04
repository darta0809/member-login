package com.darta.MemberLogin.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Documented 產生文件時，顯示在文件上
//@Inherited 此註解是開啟能讓子類別繼承
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_REVIEWER', 'ROLE_ADMIN')")
public @interface IsReviewer {
    /*
     * @interface 自定義註解功能
     * 自定義權限
     * @Target 表示該註解使用方式 ElementType.METHOD : 方法聲明
     *                          ElementType.TYPE : 類別、介面、或枚舉聲明
     *                          ElementType.TYPE_USE : JDK8 功能: 例如 List<@Email String> email = ....;
     * @Retention 定義生命週期 RetentionPolicy.RUNTIME :　運行時也保留該註解，可使用反射機制讀取該信息
     *
     * @PreAuthorize　適用進入方法前的權限驗證，可將登入用戶的 role/permissions 參數傳到方法中
     * ### 必須開啟 @EnableGlobalMethodSecurity(prePostEnabled = true) 才能使用 ###
     * */
}
