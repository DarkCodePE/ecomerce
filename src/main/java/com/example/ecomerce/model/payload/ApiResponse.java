package com.example.ecomerce.model.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  private final T data;
  private final String message;
  private final Boolean success;
  private final String timestamp;
  private final String cause;
  private final String path;

  public ApiResponse(Boolean success, T data, String message, String cause,
                     String path) {
    this.message = message;
    this.timestamp = Instant.now().toString();
    this.data = data;
    this.success = success;
    this.cause = cause;
    this.path = path;
  }

  public ApiResponse(Boolean success, T data, String message) {
    this.message = message;
    this.timestamp = Instant.now().toString();
    this.data = data;
    this.success = success;
    this.cause = null;
    this.path = null;
  }

  public T getData() {
    return data;
  }

  public Boolean getSuccess() {
    return success;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getCause() {
    return cause;
  }

  public String getPath() {
    return path;
  }

  public String getMessage() {
    return message;
  }
}
