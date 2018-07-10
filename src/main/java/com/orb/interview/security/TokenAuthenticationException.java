package com.orb.interview.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when the authentication token provided by the user is
 * not registered in the database.
 */
public class TokenAuthenticationException extends AuthenticationException {
  public TokenAuthenticationException() {
    super("invalid authentication token");
  }
}
