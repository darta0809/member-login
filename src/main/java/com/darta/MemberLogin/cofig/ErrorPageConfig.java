package com.darta.MemberLogin.cofig;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/*
 * EmbeddedServletContainerCustomizer 使用版本為 springboot 1.X 版本
 * WebServerFactoryCustomizer<ConfigurableWebServerFactory> 使用版本為 springboot 2.X 版本 
 * */

public class ErrorPageConfig {
/*
	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return new MyCustomizer();
	}
	
	private static class MyCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
		
		@Override
        public void customize(ConfigurableWebServerFactory factory) {
            factory.setPort(8080);
            factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));	
        }
	}*/
}
