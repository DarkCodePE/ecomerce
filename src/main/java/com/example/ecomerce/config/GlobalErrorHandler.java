package com.example.ecomerce.config;

import com.example.ecomerce.exception.ErrorMessageResponse;
import com.example.ecomerce.exception.InvalidTokenRequestException;
import com.example.ecomerce.exception.NoSuchElementFoundException;
import com.example.ecomerce.exception.TokenRefreshException;
import com.example.ecomerce.exception.UserLoginException;
import com.example.ecomerce.exception.UserLogoutException;
import com.example.ecomerce.utils.ErrorTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

  private final MessageSource messageSource;

  @Autowired
  public GlobalErrorHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler({UserLogoutException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessageResponse<String> onUserLogoutException(UserLogoutException e) {
    String message =
        messageSource.getMessage(e.getMessage(), e.getParams(), LocaleContextHolder.getLocale());
    return new ErrorMessageResponse<>(ErrorTypes.INVALID_ARGUMENT, message);
  }

  @ExceptionHandler({TokenRefreshException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessageResponse<String> onTokenRefreshException(UserLogoutException e) {
    String message =
        messageSource.getMessage(e.getMessage(), e.getParams(), LocaleContextHolder.getLocale());
    return new ErrorMessageResponse<>(ErrorTypes.INVALID_ARGUMENT, message);
  }

  @ExceptionHandler({InvalidTokenRequestException.class})
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public ErrorMessageResponse<String> onInvalidTokenRequestException(UserLogoutException e) {
    String message =
        messageSource.getMessage(e.getMessage(), e.getParams(), LocaleContextHolder.getLocale());
    return new ErrorMessageResponse<>(ErrorTypes.INVALID_ARGUMENT, message);
  }

  @ExceptionHandler({UserLoginException.class})
  @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
  public ErrorMessageResponse<String> onUserLoginException(UserLoginException e) {
    String message =
        messageSource.getMessage(e.getMessage(), e.getParams(), LocaleContextHolder.getLocale());
    return new ErrorMessageResponse<>(ErrorTypes.VALIDATION, message);
  }

  @ExceptionHandler(NoSuchElementFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorMessageResponse<String> handleNoSuchElementFoundException(NoSuchElementFoundException e) {
    String message =
        messageSource.getMessage(e.getMessage(), e.getParams(), LocaleContextHolder.getLocale());
    return new ErrorMessageResponse<>(ErrorTypes.VALIDATION, message);
  }
}
