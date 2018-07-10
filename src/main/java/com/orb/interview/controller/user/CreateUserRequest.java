package com.orb.interview.controller.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

public class CreateUserRequest {
  @NotBlank(message = "{com.orb.interview.user.create.EmailNotBlank}")
  @JsonProperty("email")
  public String email;

  @NotBlank(message = "{com.orb.interview.user.create.PasswordNotBlank}")
  @JsonProperty("password")
  public String password;
}
