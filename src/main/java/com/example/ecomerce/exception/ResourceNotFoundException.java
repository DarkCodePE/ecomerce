package com.example.ecomerce.exception;

public class ResourceNotFoundException extends I18AbleException{

  public ResourceNotFoundException(String key, Object... args) {
    super(key, args);
  }
}
