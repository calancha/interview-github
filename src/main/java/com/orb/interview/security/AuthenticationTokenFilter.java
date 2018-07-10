package com.orb.interview.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.orb.interview.security.TokenAuthentication.AUTH_TOKEN_HEADER;

import java.io.IOException;

/**
 * Request filter that extract an authentication token from the request header and,
 * if present, set it to the security context.
 */
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = request.getHeader(AUTH_TOKEN_HEADER);

    if (token != null && token.length() > 0) {
      TokenAuthentication authenticationToken = new TokenAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      LOGGER.info("Set the authentication token to the SecurityContext. token:{}", token);
    }

    filterChain.doFilter(request, response);
  }
}
