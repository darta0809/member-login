package com.darta.MemberLogin.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationFailureHandlerImpl /*implements AuthenticationFailureHandler*/ {
/*
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        response.setStatus(404);
        Map<String, String> result = new HashMap<>();
        result.put("error", "登入失敗");
        ObjectMapper mapper = new ObjectMapper();
        out.write(mapper.writeValueAsString(result));
        out.flush();
        out.close();
    }*/
}
