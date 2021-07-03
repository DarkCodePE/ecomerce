package com.example.ecomerce.exception;

import com.example.ecomerce.exception.I18AbleException;

public class UpdatePasswordException extends I18AbleException {

  public UpdatePasswordException(String key, Object... args) {
    super(key, args);
  }
}
