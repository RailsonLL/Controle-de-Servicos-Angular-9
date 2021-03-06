package com.clientes.config;

import java.util.Arrays;
import java.util.List;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

	/**
	 * Configuração das permissões HTTP incluindo o CrosOrigins
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		List<String> all = Arrays.asList("*");
		
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedHeaders(all);
		corsConfiguration.setAllowedOrigins(all);
		corsConfiguration.setAllowedMethods(all);
		corsConfiguration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration); //registra todas as URLs com a configuração setada acima
		
		CorsFilter corsFilter = new CorsFilter(source);
		FilterRegistrationBean<CorsFilter> filter = new FilterRegistrationBean<>(corsFilter);
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE); //seta essa configuração como prioritária
		
		return filter;
	}
	
}
