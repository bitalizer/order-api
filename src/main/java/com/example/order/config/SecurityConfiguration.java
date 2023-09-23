package com.example.order.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	// @formatter:off
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(request -> request
              .requestMatchers(
                  "/api/v1/**",
                  "/error"
              ).permitAll()
          )
          .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
          .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
              .authenticationEntryPoint(
                  (request, response, ex) -> response.sendError(
                      HttpServletResponse.SC_UNAUTHORIZED,
                      ex.getMessage()
                  )
              )
          )
          .build();
    }
    // @formatter:on

}