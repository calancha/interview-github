package com.orb.interview.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.orb.interview.infrastructure.dtos.AuthenticationToken;
import com.orb.interview.infrastructure.dtos.User;
import com.orb.interview.infrastructure.repositories.AuthenticationTokenRepository;
import com.orb.interview.infrastructure.repositories.UserRepository;
import com.orb.interview.model.user.InvalidLoginException;
import com.orb.interview.model.user.LoginFailureException;
import com.orb.interview.model.user.UserAlreadyExistsException;
import com.orb.interview.model.user.UserCreationFailureException;
import com.orb.interview.model.user.UserServiceImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServiceImpl.class)
public class UserServiceImplTest {
  @SpyBean
  private UserServiceImpl userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private AuthenticationTokenRepository authenticationTokenRepository;

  @Captor
  private ArgumentCaptor<User> userCaptor;

  @Captor
  private ArgumentCaptor<AuthenticationToken> tokenCaptor;

  @Test(expected = UserAlreadyExistsException.class)
  public void createUser_userAlreadyExists_throwUserAlreadyExistsException() throws Exception {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(new User()));

    // Act
    userService.createUser(email, password);
  }

  @Test
  public void createUser_findByEmailThrowsDataAccessException_throwUserCreationException() {
    // Arrange
    DataAccessException toThrow = new DataAccessException("some exception") {};
    String email = "some@email.com";
    String password = "some_password";
    when(userRepository.findByEmail(anyString()))
            .thenThrow(toThrow);

    // Act/Assert
    assertThatThrownBy(() -> userService.createUser(email, password))
            .isInstanceOf(UserCreationFailureException.class)
            .hasCause(toThrow);
  }

  @Test
  public void createUser_saveThrowsDataAccessException_throwUserCreationException() {
    // Arrange
    DataAccessException toThrow = new DataAccessException("some exception") {};
    String email = "some@email.com";
    String password = "some_password";
    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());
    doThrow(toThrow).when(userRepository).save(any());

    // Act/Assert
    assertThatThrownBy(() -> userService.createUser(email, password))
            .isInstanceOf(UserCreationFailureException.class)
            .hasCause(toThrow);
  }

  @Test
  public void createUser_succeed_userRepositoryProperlyCalled() throws Exception {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());

    // Act
    userService.createUser(email, password);

    // Assert
    InOrder inOrder = inOrder(userRepository);
    inOrder.verify(userRepository).findByEmail(email);
    inOrder.verify(userRepository).save(userCaptor.capture());
    User savedUser = userCaptor.getValue();
    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getId()).isNotBlank();
    assertThat(savedUser.getEmail()).isEqualTo(email);
    assertThat(savedUser.getPassword()).isEqualTo(password);
    assertThat(savedUser.getBalance()).isEqualTo(0);
  }

  @Test(expected = InvalidLoginException.class)
  public void login_emailNotFound_throwInvalidLoginException() throws Exception {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());

    // Act
    userService.login(email, password);
  }

  @Test(expected = InvalidLoginException.class)
  public void login_invalidPassword_throwInvalidLoginException() throws Exception {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    String otherPassword = "wrong_password";
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword(otherPassword);
    user.setEmail(email);
    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));

    // Act
    userService.login(email, password);
  }

  @Test
  public void login_findUserThrowsDataAccessException_throwLoginFailureException() {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    DataAccessException toThrow = new DataAccessException("some_message") {};
    when(userRepository.findByEmail(anyString()))
            .thenThrow(toThrow);

    // Act/Assert
    assertThatThrownBy(
            () -> userService.login(email, password)
    ).isInstanceOf(LoginFailureException.class).hasCause(toThrow);
  }

  @Test
  public void login_findTokenThrowsDataAccessException_throwLoginFailureException() {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword(password);
    user.setEmail(email);
    DataAccessException toThrow = new DataAccessException("some_message") {};
    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));
    when(authenticationTokenRepository.findFirstByUserId(anyString()))
            .thenThrow(toThrow);

    // Act/Assert
    assertThatThrownBy(
            () -> userService.login(email, password)
    ).isInstanceOf(LoginFailureException.class).hasCause(toThrow);
  }

  @Test
  public void login_generateNewTokenThrowsDataAccessException_throwLoginFailureException() {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword(password);
    user.setEmail(email);
    DataAccessException toThrow = new DataAccessException("some_message") {};
    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));
    when(authenticationTokenRepository.findFirstByUserId(anyString()))
            .thenReturn(Optional.empty());
    doThrow(toThrow).when(authenticationTokenRepository).save(any());

    // Act/Assert
    assertThatThrownBy(
            () -> userService.login(email, password)
    ).isInstanceOf(LoginFailureException.class).hasCause(toThrow);
  }

  @Test
  public void login_succeeds_verifyLogin() throws Exception {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword(password);
    user.setEmail(email);

    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));
    when(authenticationTokenRepository.findFirstByUserId(anyString()))
            .thenReturn(Optional.empty());

    // Act
    userService.login(email, password);

    // Assert
    verify(userRepository).findByEmail(email);
  }

  @Test
  public void login_tokenExists_returnToken() throws Exception {
    String email = "some@email.com";
    String password = "some_password";
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword(password);
    user.setEmail(email);
    AuthenticationToken token = new AuthenticationToken();
    token.setPayload(UUID.randomUUID().toString());
    token.setUser(user);
    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));
    when(authenticationTokenRepository.findFirstByUserId(anyString()))
            .thenReturn(Optional.of(token));

    // Act
    String payload = userService.login(email, password);

    // Assert
    assertThat(payload).isEqualTo(token.getPayload());
  }

  @Test
  public void login_tokenNotExists_createNewToken() throws Exception {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword(password);
    user.setEmail(email);
    String newTokenPayoad = UUID.randomUUID().toString();

    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));
    when(authenticationTokenRepository.findFirstByUserId(anyString()))
            .thenReturn(Optional.empty());
    when(userService.generateTokenPayload()).thenReturn(newTokenPayoad);

    // Act
    userService.login(email, password);

    // Assert
    InOrder inOrder = inOrder(authenticationTokenRepository);
    inOrder.verify(authenticationTokenRepository).findFirstByUserId(user.getId());
    inOrder.verify(authenticationTokenRepository).save(tokenCaptor.capture());
    AuthenticationToken savedToken = tokenCaptor.getValue();
    assertThat(savedToken).isNotNull();
    assertThat(savedToken.getPayload()).isEqualTo(newTokenPayoad);
    assertThat(savedToken.getUser()).isNotNull();
    assertThat(savedToken.getUser().getId()).isEqualTo(user.getId());
  }

  @Test
  public void login_tokenNotExists_returnNewlyGeneratedPayload() throws Exception {
    // Arrange
    String email = "some@email.com";
    String password = "some_password";
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setBalance(100);
    user.setPassword(password);
    user.setEmail(email);
    String newTokenPayoad = UUID.randomUUID().toString();

    when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(user));
    when(authenticationTokenRepository.findFirstByUserId(anyString()))
            .thenReturn(Optional.empty());
    when(userService.generateTokenPayload()).thenReturn(newTokenPayoad);

    // Act
    String gotPayload = userService.login(email, password);

    // Assert
    assertThat(gotPayload).isEqualTo(newTokenPayoad);
  }
}