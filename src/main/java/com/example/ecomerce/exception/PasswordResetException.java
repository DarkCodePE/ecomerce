package com.example.ecomerce.exception;

public class PasswordResetException extends I18AbleException{

  public PasswordResetException(String key, Object... args) {
    super(key, args);
  }
}
