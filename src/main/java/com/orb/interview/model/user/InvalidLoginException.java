package com.orb.interview.model.user;

import org.springframework.http.HttpStatus;

import com.orb.interview.model.BusinessException;

public class InvalidLoginException extends BusinessException {
  public static final String INVALID_LOGIN_KEU = "com.orb.interview.user.login.Invalid";

  public InvalidLoginException() {
    super(HttpStatus.UNAUTHORIZED, INVALID_LOGIN_KEU);
  }
}
