package com.example.ecomerce.exception;

public class TokenRefreshException extends I18AbleException{
  public TokenRefreshException(String key, Object... args) {
    super(key, args);
  }
}
