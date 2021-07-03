package com.example.ecomerce.exception;

public class UserLoginException extends I18AbleException{

  public UserLoginException(String key, Object... args) {
    super(key, args);
  }
}
