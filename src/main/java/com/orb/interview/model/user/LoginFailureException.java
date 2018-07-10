package com.orb.interview.model.user;

import org.springframework.http.HttpStatus;

import com.orb.interview.model.SystemException;

public class LoginFailureException extends SystemException {
  public static final String LOGIN_FAILURE_KEY = "com.orb.interview.user.login.Failure";

  public LoginFailureException(Throwable cause) {
    super(LOGIN_FAILURE_KEY, cause);
  }
}
