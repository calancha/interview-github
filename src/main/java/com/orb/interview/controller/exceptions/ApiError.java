package com.orb.interview.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Json format of an error response.
 */
public class ApiError {
  @JsonProperty("message")
  public String message;

  public ApiError(String message) {
    this.message = message;
  }
}
