package com.example.ecomerce.exception;

public class NoSuchElementFoundException extends I18AbleException{

  public NoSuchElementFoundException(String key, Object... args) {
    super(key, args);
  }
}
