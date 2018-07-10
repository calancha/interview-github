package com.orb.interview.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.orb.interview.infrastructure.dtos.AuthenticationToken;
import com.orb.interview.infrastructure.dtos.User;
import com.orb.interview.infrastructure.repositories.AuthenticationTokenRepository;
import com.orb.interview.security.AuthenticatedUser;
import com.orb.interview.security.AuthenticatedUserProvider;
import com.orb.interview.security.TokenAuthentication;
import com.orb.interview.security.TokenAuthenticationException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AuthenticatedUserProvider.class)
public class AuthenticatedUserProviderTest {
  @Autowired
  private AuthenticatedUserProvider authenticatedUserProvider;

  @MockBean
  private AuthenticationTokenRepository authenticationTokenRepository;

  @Test(expected = TokenAuthenticationException.class)
  public void authenticate_tokenNotFound_throwTokenAuthenticationException() {
    // Arrange
    TokenAuthentication authentication = new TokenAuthentication(UUID.randomUUID().toString());
    when(authenticationTokenRepository.findById(anyString()))
            .thenReturn(Optional.empty());

    // Act
    authenticatedUserProvider.authenticate(authentication);
  }

  @Test
  public void authenticate_succeed_returnAuthenticatedUser() {
    // Arrange
    String token = UUID.randomUUID().toString();
    TokenAuthentication authentication = new TokenAuthentication(token);
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword("password");
    user.setEmail("some@email.com");
    AuthenticationToken dtoToken = new AuthenticationToken();
    dtoToken.setPayload(token);
    dtoToken.setUser(user);
    when(authenticationTokenRepository.findById(anyString()))
            .thenReturn(Optional.of(dtoToken));

    // Act
    Authentication returnedAuth = authenticatedUserProvider.authenticate(authentication);

    // Assert
    assertThat(returnedAuth).isNotNull();
    assertThat(returnedAuth).isInstanceOf(AuthenticatedUser.class);
    AuthenticatedUser returnedAuthUser = (AuthenticatedUser) returnedAuth;
    assertThat(returnedAuthUser.getPrincipal()).isInstanceOf(User.class);
    User returnedUser = (User) returnedAuthUser.getPrincipal();
    assertThat(returnedUser.getId()).isEqualTo(user.getId());
    assertThat(returnedUser.getBalance()).isEqualTo(user.getBalance());
    assertThat(returnedUser.getPassword()).isEqualTo(user.getPassword());
    assertThat(returnedUser.getEmail()).isEqualTo(user.getEmail());
  }

  @Test
  public void authenticate_succeed_retrieveCorrespondingUser() {
    // Arrange
    String token = UUID.randomUUID().toString();
    TokenAuthentication authentication = new TokenAuthentication(token);
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword("password");
    user.setEmail("some@email.com");
    AuthenticationToken dtoToken = new AuthenticationToken();
    dtoToken.setPayload(token);
    dtoToken.setUser(user);
    when(authenticationTokenRepository.findById(anyString()))
            .thenReturn(Optional.of(dtoToken));

    // Act
    Authentication returnedAuth = authenticatedUserProvider.authenticate(authentication);

    // Assert
    verify(authenticationTokenRepository).findById(token);
  }
}
