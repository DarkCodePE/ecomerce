package com.example.ecomerce.config;

import com.example.ecomerce.security.JwtAuthenticationEntryPoint;
import com.example.ecomerce.security.JwtAuthenticationFilter;
import com.example.ecomerce.services.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j(topic = "WEB_SECURITY_DEV_CONFIG")
@Profile("!dev")
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityDevConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  public WebSecurityDevConfig(
      CustomUserDetailsService userDetailsService,
      JwtAuthenticationEntryPoint jwtEntryPoint) {
  }
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    log.info("Loaded inside a dev only");
    http.authorizeRequests()
        .anyRequest().permitAll();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

}
