package com.example.ecomerce.exception;

public class PasswordResetLinkException extends I18AbleException{

  public PasswordResetLinkException(String key, Object... args) {
    super(key, args);
  }
}
