package com.orb.interview.infrastructure.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.orb.interview.infrastructure.dtos.AuthenticationToken;
import com.orb.interview.infrastructure.dtos.User;
import com.orb.interview.infrastructure.repositories.AuthenticationTokenRepository;
import com.orb.interview.infrastructure.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
public class AuthenticationTokenRepositoryTest {
  @Autowired
  private AuthenticationTokenRepository authenticationTokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void save_whenCalled_returnSavedToken() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("user@email.com");
    user.setPassword("password");
    user.setBalance(100);
    userRepository.save(user);

    AuthenticationToken token = new AuthenticationToken();
    token.setPayload(UUID.randomUUID().toString());
    token.setUser(user);

    // Act
    AuthenticationToken returnedToken = authenticationTokenRepository.save(token);

    // Arrange
    assertThat(returnedToken).isNotNull();
    assertThat(returnedToken.getPayload()).isEqualTo(token.getPayload());
  }

  @Test
  public void findById_whenCalled_returnTokenWithCorrespondingTokenString() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("user@email.com");
    user.setPassword("password");
    user.setBalance(100);
    userRepository.save(user);

    AuthenticationToken token = new AuthenticationToken();
    token.setPayload(UUID.randomUUID().toString());
    token.setUser(user);
    authenticationTokenRepository.save(token);

    // Act
    Optional<AuthenticationToken> optToken = authenticationTokenRepository.findById(token.getPayload());

    // Arrange
    assertThat(optToken).isPresent();
    assertThat(optToken.get().getPayload()).isEqualTo(token.getPayload());
  }

  @Test
  public void getUser_whenCalled_returnUserAssociatedWithToken() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("user@email.com");
    user.setPassword("password");
    user.setBalance(100);
    userRepository.save(user);

    AuthenticationToken token = new AuthenticationToken();
    token.setPayload(UUID.randomUUID().toString());
    token.setUser(user);
    authenticationTokenRepository.save(token);

    // Act
    Optional<AuthenticationToken> optToken = authenticationTokenRepository.findById(token.getPayload());

    // Arrange
    assertThat(optToken).isPresent();
    User foundUser = optToken.get().getUser();
    assertThat(foundUser).isNotNull();
    assertThat(foundUser.getId()).isEqualTo(user.getId());
    assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());
    assertThat(foundUser.getBalance()).isEqualTo(user.getBalance());
  }

  @Test
  public void findFirstByUserId_whenCalled_returnToken() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("user@email.com");
    user.setPassword("password");
    user.setBalance(100);
    userRepository.save(user);

    AuthenticationToken token = new AuthenticationToken();
    token.setPayload(UUID.randomUUID().toString());
    token.setUser(user);
    authenticationTokenRepository.save(token);

    // Act
    Optional<AuthenticationToken> optToken = authenticationTokenRepository.findFirstByUserId(user.getId());

    // Assert
    assertThat(optToken).isPresent();
    User foundUser = optToken.get().getUser();
    assertThat(foundUser).isNotNull();
    assertThat(foundUser.getId()).isEqualTo(user.getId());
    assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());
    assertThat(foundUser.getBalance()).isEqualTo(user.getBalance());
  }
}