package com.orb.interview.model.user;

import com.orb.interview.model.SystemException;

/**
 * Class for unexpected failure to create a user.
 */
public class UserCreationFailureException extends SystemException {
  public static final String USER_CREATION_FAILURE_KEY = "com.orb.interview.user.create.Failure";

  /**
   * Creates a new instance of <code>UserCreationFailureException</code>.
   * @param cause The cause of this exception.
   */
  public UserCreationFailureException(Throwable cause) {
    super(USER_CREATION_FAILURE_KEY, cause);
  }

}
