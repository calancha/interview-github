package com.orb.interview.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.orb.interview.infrastructure.dtos.User;
import com.orb.interview.infrastructure.repositories.AuthenticationTokenRepository;

/**
 * Authentication provider fetching the corresponding user of a given {@link TokenAuthentication}.
 */
@Component
public class AuthenticatedUserProvider implements AuthenticationProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedUserProvider.class);

  @Autowired
  private AuthenticationTokenRepository authenticationTokenRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getCredentials();
    User user = authenticationTokenRepository.findById(token)
            .orElseThrow(() -> {
              LOGGER.error("authentication token not found. token={}", token);
              return new TokenAuthenticationException();
            })
            .getUser();
    return new AuthenticatedUser(user);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return TokenAuthentication.class.equals(authentication);
  }
}
