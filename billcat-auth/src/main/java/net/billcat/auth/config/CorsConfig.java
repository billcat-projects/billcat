package net.billcat.auth.config;

import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

public class CorsConfig {

	public static CorsConfiguration create() {
		var cors = new CorsConfiguration();
		cors.setAllowedOriginPatterns(List.of("*")); // bff swagger 地址
		cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		cors.setAllowedHeaders(List.of("*"));
		cors.setAllowCredentials(true);
		return cors;
	}

}