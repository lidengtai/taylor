package com.tai.common;

import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

public class CustomServletContainer implements WebServerFactoryCustomizer<WebServerFactory>{

	@Override
	public void customize(WebServerFactory factory) {
		// TODO Auto-generated method stub
		
	}
	@Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer(){
        return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>() {
            @Override
            public void customize(ConfigurableServletWebServerFactory factory) {
                factory.setPort(8090);
            }
        };
    }
}
