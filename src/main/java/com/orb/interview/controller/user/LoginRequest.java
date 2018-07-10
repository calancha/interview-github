package com.orb.interview.controller.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

/**
 * Json format for a login request.
 */
public class LoginRequest {
  /**
   * The email of the user to login.
   */
  @NotBlank(message = "{com.orb.interview.user.login.EmailNotBlank}")
  @JsonProperty("email")
  public String email;

  /**
   * The password of the user to login.
   */
  @NotBlank(message = "{com.orb.interview.user.login.PasswordNotBlank}")
  @JsonProperty("password")
  public String password;
}
