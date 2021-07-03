package com.example.ecomerce.exception;

public class OAuth2AuthenticationProcessingException extends I18AbleException{

  public OAuth2AuthenticationProcessingException(String key, Object... args) {
    super(key, args);
  }
}
