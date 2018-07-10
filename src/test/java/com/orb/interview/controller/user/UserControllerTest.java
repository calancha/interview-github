package com.orb.interview.controller.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.orb.interview.controller.user.CreateUserRequest;
import com.orb.interview.controller.user.LoginRequest;
import com.orb.interview.controller.user.LoginResponse;
import com.orb.interview.controller.user.UserController;
import com.orb.interview.infrastructure.dtos.User;
import com.orb.interview.model.user.InvalidLoginException;
import com.orb.interview.model.user.LoginFailureException;
import com.orb.interview.model.user.UserAlreadyExistsException;
import com.orb.interview.model.user.UserCreationFailureException;
import com.orb.interview.model.user.UserService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserController.class)
public class UserControllerTest {
  @Autowired
  private UserController userController;

  @MockBean
  private UserService userService;

  @Captor
  ArgumentCaptor<User> userCaptor;

  @Test
  public void createUser_succeed_callServiceCreateUser() throws Exception {
    // Arrange
    CreateUserRequest request = new CreateUserRequest();
    request.email = "some@email.com";
    request.password = "some@password.com";

    // Act
    userController.createUser(request);

    // Assert
    verify(userService).createUser(request.email, request.password);
  }

  @Test
  public void createUser_serviceThrowsUserAlreadyExistsException_throwException() throws Exception {
    // Arrange
    CreateUserRequest request = new CreateUserRequest();
    request.email = "some@email.com";
    request.password = "some@password.com";

    UserAlreadyExistsException toThrow = new UserAlreadyExistsException();
    doThrow(toThrow).when(userService).createUser(any(), any());

    // Act/Assert
    assertThatThrownBy(() -> userController.createUser(request))
            .isEqualTo(toThrow);
  }

  @Test
  public void createUser_serviceThrowsUserCreationException_throwException() throws Exception {
    // Arrange
    CreateUserRequest request = new CreateUserRequest();
    request.email = "some@email.com";
    request.password = "some@password.com";

    UserCreationFailureException toThrow = new UserCreationFailureException(new Exception());
    doThrow(toThrow).when(userService).createUser(any(), any());

    // Act/Assert
    assertThatThrownBy(() -> userController.createUser(request))
            .isEqualTo(toThrow);
  }

  @Test
  public void login_succeeds_returnToken() throws Exception {
    // Arrange
    LoginRequest request = new LoginRequest();
    request.email = "some@email.com";
    request.password = "some@password.com";
    String token = UUID.randomUUID().toString();

    when(userService.login(any(), any())).thenReturn(token);

    // Act
    LoginResponse response = userController.login(request);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.token).isEqualTo(token);
  }

  @Test
  public void login_succeeds_callServiceLoginFunction() throws Exception {
    // Arrange
    LoginRequest request = new LoginRequest();
    request.email = "some@email.com";
    request.password = "some@password.com";
    String token = UUID.randomUUID().toString();

    when(userService.login(any(), any())).thenReturn(token);

    // Act
    userController.login(request);

    // Assert
    verify(userService).login(request.email, request.password);
  }

  @Test
  public void login_serviceThrowsInvalidLoginException_throwException() throws Exception {
    // Arrange
    LoginRequest request = new LoginRequest();
    request.email = "some@email.com";
    request.password = "some@password.com";

    InvalidLoginException toThrow = new InvalidLoginException();
    when(userService.login(any(), any())).thenThrow(toThrow);

    // Act/Assert
    assertThatThrownBy(() -> userController.login(request)).isEqualTo(toThrow);
  }

  @Test
  public void login_serviceThrowsLoginFailureException_throwException() throws Exception {
    // Arrange
    LoginRequest request = new LoginRequest();
    request.email = "some@email.com";
    request.password = "some@password.com";

    LoginFailureException toThrow = new LoginFailureException(new Exception());
    when(userService.login(any(), any())).thenThrow(toThrow);

    // Act/Assert
    assertThatThrownBy(() -> userController.login(request)).isEqualTo(toThrow);
  }
}
