package com.example.ecomerce.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j(topic = "JWT_AUTHENTICATION_ENTRY_POINT")
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sorry, You're not authorized to access this resource.");
  }
}
