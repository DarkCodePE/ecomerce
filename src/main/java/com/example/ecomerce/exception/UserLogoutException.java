package com.example.ecomerce.exception;

public class UserLogoutException extends I18AbleException{

  public UserLogoutException(String key, Object... args) {
    super(key, args);
  }
}
