package com.example.ecomerce.config;

import com.example.ecomerce.security.JwtAuthenticationEntryPoint;
import com.example.ecomerce.security.JwtAuthenticationFilter;
import com.example.ecomerce.security.oauth2.CustomOAuth2UserService;
import com.example.ecomerce.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.ecomerce.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.ecomerce.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.example.ecomerce.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Profile("dev")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService userDetailsService;

  private final JwtAuthenticationEntryPoint jwtEntryPoint;

  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

  @Autowired
  private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  @Autowired
  private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  @Autowired
  private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Autowired
  public WebSecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationEntryPoint jwtEntryPoint) {
    this.userDetailsService = userDetailsService;
    this.jwtEntryPoint = jwtEntryPoint;
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Bean
  public JwtAuthenticationFilter  jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
        "/swagger-ui.html", "/webjars/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling()
        .authenticationEntryPoint(jwtEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/**").permitAll()
        .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
        .antMatchers(HttpMethod.POST, "/oauth2/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorize")
        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
        .and()
        .redirectionEndpoint()
        .baseUri("/oauth2/callback/*")
        .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .and()
        .successHandler(oAuth2AuthenticationSuccessHandler)
        .failureHandler(oAuth2AuthenticationFailureHandler);

    http.addFilterBefore(jwtAuthenticationFilter(),
        UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
