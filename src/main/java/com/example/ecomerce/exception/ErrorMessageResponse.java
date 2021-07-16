package com.example.ecomerce.exception;

import com.example.ecomerce.utils.ErrorTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponse<T> {
  private ErrorTypes errorType;
  private T message;
}
