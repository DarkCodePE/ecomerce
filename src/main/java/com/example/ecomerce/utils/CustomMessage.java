package com.example.ecomerce.utils;

import java.util.Locale;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomMessage {
  private final MessageSource messageSource;
  public String getLocalMessage(String key, String... params){
    return messageSource.getMessage(key, params, Locale.ENGLISH);
  }
}
