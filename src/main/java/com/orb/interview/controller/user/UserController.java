package com.orb.interview.controller.user;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.orb.interview.model.user.InvalidLoginException;
import com.orb.interview.model.user.LoginFailureException;
import com.orb.interview.model.user.UserAlreadyExistsException;
import com.orb.interview.model.user.UserCreationFailureException;
import com.orb.interview.model.user.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
  public void createUser(@Validated @RequestBody CreateUserRequest request)
          throws UserAlreadyExistsException, UserCreationFailureException {
    userService.createUser(request.email, request.password);
  }

  @RequestMapping(path = "login", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public LoginResponse login(@Validated @RequestBody LoginRequest request) throws InvalidLoginException, LoginFailureException {
    String token = userService.login(request.email, request.password);
    return new LoginResponse(token);
  }
}
