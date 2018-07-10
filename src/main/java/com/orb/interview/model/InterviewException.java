package com.orb.interview.model;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;

/**
 * Super class for all exceptions to be handled by the controller.
 */
public abstract class InterviewException extends Exception implements MessageSourceResolvable {
  private final HttpStatus httpStatus;
  private final String messageKey;

//  /**
//   * Creates a new instance of <code>InterviewException</code>.
//   * @param httpStatus The http status to return when this exception is thrown.
//   */

  /**
   * Creates a new instance of <code>InterviewException</code>.
   * @param httpStatus The http status to return when this exception is thrown.
   * @param messageKey The message key to use for fetching the corresponding error message.
   */
  public InterviewException(HttpStatus httpStatus, String messageKey) {
    this(httpStatus, messageKey, null);
  }

  /**
   * Creates a new instance of <code>InterviewException</code>.
   * @param httpStatus The http status to return when this exception is thrown.
   * @param messageKey The message key to use for fetching the corresponding error message.
   * @param cause The cause of this exception.
   */
  public InterviewException(HttpStatus httpStatus, String messageKey, Throwable cause) {
    super(cause);
    this.messageKey = messageKey;
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getMessageKey() {
    return messageKey;
  }

  @Override
  public String getDefaultMessage() {
    return messageKey;
  }

  @Override
  public String[] getCodes() {
    return new String[]{messageKey};
  }
}
