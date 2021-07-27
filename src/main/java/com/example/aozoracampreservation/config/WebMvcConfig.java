package com.example.aozoracampreservation.config;

import com.example.aozoracampreservation.presentation.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	public FilterRegistrationBean LoggingFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean(new LoggingFilter());
		bean.setOrder(1);
		return bean;
	}
}
