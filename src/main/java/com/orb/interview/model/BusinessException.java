package com.orb.interview.model;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by the user of the application.
 */
public abstract class BusinessException extends InterviewException {
  /**
   * Creates a new instance of <code>BusinessException</code>.
   * @param httpStatus The http status to return when this exception is thrown.
   * @param messageKey The message key to use for fetching the corresponding error message.
   */
  public BusinessException(HttpStatus httpStatus, String messageKey) {
    super(httpStatus, messageKey);
  }

  /**
   * Creates a new instance of <code>BusinessException</code>.
   * @param httpStatus The http status to return when this exception is thrown.
   * @param messageKey The message key to use for fetching the corresponding error message.
   * @param cause The cause of this exception.
   */
  public BusinessException(HttpStatus httpStatus, String messageKey, Throwable cause) {
    super(httpStatus, messageKey, cause);
  }
}
