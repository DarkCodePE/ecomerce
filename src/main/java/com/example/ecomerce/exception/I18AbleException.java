package com.example.ecomerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class I18AbleException extends RuntimeException{
  protected Object[] params;

  public I18AbleException(String key, Object... args) {
    super(key);
    params = args;
  }
}