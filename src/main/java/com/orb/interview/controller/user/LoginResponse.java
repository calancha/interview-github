package com.orb.interview.controller.user;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Json format for a login response
 */
public class LoginResponse {
  /**
   * The authentication token of the user.
   */
  @JsonProperty("token")
  public String token;

  public LoginResponse(String token) {
    this.token = token;
  }
}
