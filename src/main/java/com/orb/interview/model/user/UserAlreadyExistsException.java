package com.orb.interview.model.user;

import org.springframework.http.HttpStatus;

import com.orb.interview.model.BusinessException;

public class UserAlreadyExistsException extends BusinessException {
  public static final String USER_ALREADY_EXISTS_KEY = "com.orb.interview.user.create.AlreadyExists";

  public UserAlreadyExistsException() {
    super(HttpStatus.BAD_REQUEST, USER_ALREADY_EXISTS_KEY);
  }
}
