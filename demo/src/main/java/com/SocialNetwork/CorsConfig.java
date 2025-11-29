//package com.SocialNetwork;
//	import org.springframework.context.annotation.Bean;
//	import org.springframework.context.annotation.Configuration;
//	import org.springframework.web.servlet.config.annotation.CorsRegistry;
//	import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//	@Configuration
//	public class CorsConfig {
//
//	    @Bean
//	    public WebMvcConfigurer corsConfigurer() {
//	        return new WebMvcConfigurer() {
//	            @Override
//	            public void addCorsMappings(CorsRegistry registry) {
//	                registry.addMapping("/**")
//	                        .allowedOrigins("http://localhost:4200") // your Angular app URL
//	                        .allowedMethods("*")
//	                        .allowedHeaders("*")
//	                        .allowCredentials(true);
//	            }
//	
//	        };
//	    }
//	}
package com.SocialNetwork;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Global CORS for all endpoints
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // exact Angular URL
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                // Explicitly include WebSocket/SockJS endpoints
                registry.addMapping("/ws/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}


