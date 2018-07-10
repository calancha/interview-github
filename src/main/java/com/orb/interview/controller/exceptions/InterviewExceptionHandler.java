package com.orb.interview.controller.exceptions;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.orb.interview.model.BusinessException;
import com.orb.interview.model.InterviewException;
import com.orb.interview.model.SystemException;

/**
 * Exception handler of the application.
 */
@ControllerAdvice
public class InterviewExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(InterviewExceptionHandler.class);
  private static final String UNKNOWN_EXCEPTION_MESSAGE_KEY = "com.orb.interview.UnknownError";

  @Autowired
  private MessageSource messageSource;

  @ExceptionHandler(BusinessException.class)
  @Order(Ordered.HIGHEST_PRECEDENCE)
  ResponseEntity<ApiError> handleBusinessException(BusinessException ex, Locale locale) {
    LOGGER.warn("A business error occured.", ex);
    return handle(ex, locale);
  }

  @ExceptionHandler(SystemException.class)
  @Order(Ordered.HIGHEST_PRECEDENCE)
  ResponseEntity<ApiError> handleSystemException(SystemException ex, Locale locale) {
    LOGGER.error("A system error occured.", ex);
    return handle(ex, locale);
  }

  @ExceptionHandler(Throwable.class)
  @Order
  ResponseEntity<ApiError> handleUnknownException(Throwable ex, Locale locale) {
    LOGGER.error("An unknown exception occurred.", ex);
    return handle(new InterviewException(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_EXCEPTION_MESSAGE_KEY) {}, locale);
  }

  private ResponseEntity<ApiError> handle(InterviewException ex, Locale locale) {
    String message = messageSource.getMessage(ex, locale);

    ApiError error = new ApiError(message);
    return new ResponseEntity<>(error, ex.getHttpStatus());
  }
}
