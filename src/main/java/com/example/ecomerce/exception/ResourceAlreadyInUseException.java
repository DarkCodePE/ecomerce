package com.example.ecomerce.exception;

public class ResourceAlreadyInUseException extends I18AbleException{
  public ResourceAlreadyInUseException(String key, Object... args) {
    super(key, args);
  }
}
