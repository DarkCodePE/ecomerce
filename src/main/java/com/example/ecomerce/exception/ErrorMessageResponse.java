package com.example.ecomerce.exception;

import com.example.ecomerce.utils.ErrorTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponse<Message> {
  private ErrorTypes errorType;
  private Message message;
}
