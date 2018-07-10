package com.orb.interview.model;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by an internal server error. The returned http code is always 500.
 */
public abstract class SystemException extends InterviewException {
  /**
   * Creates a new instance of <code>SystemException</code>.
   * @param messageKey The message key to use for fetching the corresponding error message.
   */
  public SystemException(String messageKey) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, messageKey);
  }

  /**
   * Creates a new instance of <code>SystemException</code>.
   * @param messageKey The message key to use for fetching the corresponding error message.
   * @param cause The cause of this exception.
   */
  public SystemException(String messageKey, Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, messageKey, cause);
  }
}
