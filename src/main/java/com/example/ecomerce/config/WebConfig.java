package com.example.ecomerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry corsRegistry) {
    corsRegistry.addMapping("/auth/**")
        .allowedOrigins("http://localhost:4200")
        .allowedMethods("*")
        .maxAge(3600L)
        .allowedHeaders("*")
        .exposedHeaders("Authorization")
        .allowCredentials(true);
    /*corsRegistry.addMapping("/**").allowedOrigins("http://localhost:4200");*/
  }

}