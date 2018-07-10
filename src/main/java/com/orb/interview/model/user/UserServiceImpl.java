package com.orb.interview.model.user;

import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orb.interview.infrastructure.dtos.AuthenticationToken;
import com.orb.interview.infrastructure.dtos.User;
import com.orb.interview.infrastructure.repositories.AuthenticationTokenRepository;
import com.orb.interview.infrastructure.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthenticationTokenRepository authenticationTokenRepository;

  @Transactional
  @Override
  public void createUser(String email, String password) throws UserAlreadyExistsException, UserCreationFailureException {
    try {
      if (userRepository.findByEmail(email).isPresent()) {
        throw new UserAlreadyExistsException();
      }

      User user = new User();
      user.setEmail(email);
      user.setPassword(password);
      user.setBalance(0);
      user.setId(UUID.randomUUID().toString());
      userRepository.save(user);
    } catch (DataAccessException e) {
      throw new UserCreationFailureException(e);
    }
  }

  @Transactional
  @Override
  public String login(String email, String password) throws InvalidLoginException, LoginFailureException {
    try {
      User user = userRepository.findByEmail(email)
              .orElseThrow(InvalidLoginException::new);
     
      if (!password.equals(user.getPassword())) {
        throw new InvalidLoginException();
      }

      AuthenticationToken token = authenticationTokenRepository
              .findFirstByUserId(user.getId())
              .orElseGet(() -> generateNewToken(user));
      return token.getPayload();
    } catch (DataAccessException e) {
      throw new LoginFailureException(e);
    }
  }

  private AuthenticationToken generateNewToken(User user) {
    AuthenticationToken newToken = new AuthenticationToken();
    newToken.setPayload(generateTokenPayload());
    newToken.setUser(user);
    authenticationTokenRepository.save(newToken);
    return newToken;
  }

  String generateTokenPayload() {
    return UUID.randomUUID().toString();
  }
}
