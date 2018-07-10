package com.orb.interview.security;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import com.orb.interview.security.AuthenticationTokenFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationTokenFilterTest {
  private AuthenticationTokenFilter authenticationTokenFilter;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.authenticationTokenFilter = new AuthenticationTokenFilter();
  }

  @After
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  public void doFilterInternal_withoutAuthenticationHeader_doNotSetAuthentication() throws Exception {
    // Act
    authenticationTokenFilter.doFilterInternal(request, response, filterChain);

    // Assert
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  public void doFilterInternal_withoutAuthenticationHeader_processFilterChain() throws Exception {
    // Act
    authenticationTokenFilter.doFilterInternal(request, response, filterChain);

    // Assert
    verify(filterChain).doFilter(request, response);
  }

  @Test
  public void doFilterInternal_withAuthenticationHeader_setAuthentication() throws Exception {
    // Arrange
    String token = UUID.randomUUID().toString();
    when(request.getHeader(anyString())).thenReturn(token);

    // Act
    authenticationTokenFilter.doFilterInternal(request, response, filterChain);

    // Assert
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials()).isEqualTo(token);
  }

  @Test
  public void doFilterInternal_withAuthenticationHeader_processFilterChain() throws Exception {
    // Arrange
    String token = UUID.randomUUID().toString();
    when(request.getHeader(anyString())).thenReturn(token);

    // Act
    authenticationTokenFilter.doFilterInternal(request, response, filterChain);

    // Assert
    verify(filterChain).doFilter(request, response);
  }
}
