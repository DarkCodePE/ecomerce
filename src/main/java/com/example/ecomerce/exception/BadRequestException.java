package com.example.ecomerce.exception;

public class BadRequestException extends I18AbleException{

  public BadRequestException(String key, Object... args) {
    super(key, args);
  }
}
