package com.orb.interview.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Json format for a request parameter validation error.
 */
public class ValidationErrorResponse {
  @JsonProperty("errors")
  public List<Error> errors;

  public static class Error {
    @JsonProperty("message")
    public String message;

    @JsonProperty("field")
    public String field;

    @JsonProperty("rejected_value")
    public Object rejectedValue;

    /**
     * Creates a new instance of <code>ValidationError</code>.
     * @param message The error message.
     * @param field The wrong field.
     * @param rejectedValue The wrong value.
     */
    public Error(String message, String field, Object rejectedValue) {
      this.message = message;
      this.field = field;
      this.rejectedValue = rejectedValue;
    }
  }

  public ValidationErrorResponse(List<Error> errors) {
    this.errors = errors;
  }
}
