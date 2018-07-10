package com.orb.interview.model.user;

/**
 * Service for user related operations.
 */
public interface UserService {
  /**
   * Creates a new user.
   * @param email The email of the new user.
   * @param password The password of the new user.
   */
  void createUser(String email, String password) throws UserAlreadyExistsException, UserCreationFailureException;

  String login(String email, String password) throws InvalidLoginException, LoginFailureException;
}
