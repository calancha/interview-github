package com.orb.interview.infrastructure.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.orb.interview.infrastructure.dtos.User;
import com.orb.interview.infrastructure.repositories.UserRepository;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {
  private static final Random RAND = new Random(System.currentTimeMillis());

  @Autowired
  private UserRepository userRepository;

  @Test
  public void save_whenCalled_returnInsertedUser() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("arnaud@mydomain.com");
    user.setPassword("password");
    user.setBalance(RAND.nextLong());

    // Act
    User returnedUser = userRepository.save(user);

    // Assert
    assertUser(user, returnedUser);
  }

  @Test
  public void saveAndFindById_whenCalled_returnInsertedUser() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("arnaud@mydomain.com");
    user.setPassword("password");
    user.setBalance(RAND.nextLong());

    // Act
    userRepository.save(user);
    Optional<User> optUser = userRepository.findById(user.getId());

    // Assert
    assertThat(optUser).isPresent();
    assertUser(user, optUser.get());
  }

  @Test
  public void saveAndUpdateAndFindById_whenCalled_returnUpdatedUser() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("arnaud@mydomain.com");
    user.setPassword("password");
    user.setBalance(RAND.nextLong());

    // Act
    userRepository.save(user);
    user.setBalance(100);
    userRepository.save(user);
    Optional<User> optUser = userRepository.findById(user.getId());

    // Assert
    assertThat(optUser).isPresent();
    assertUser(user, optUser.get());
  }

  @Test
  public void saveAndFindByEmail_whenCalled_returnInsertedUser() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("arnaud@mydomain.com");
    user.setPassword("password");
    user.setBalance(RAND.nextLong());

    // Act
    userRepository.save(user);
    Optional<User> optUser = userRepository.findByEmail(user.getEmail());

    // Assert
    assertThat(optUser).isPresent();
    assertUser(user, optUser.get());
  }

  private static void assertUser(User expectedUser, User returnedUser) {
    assertThat(returnedUser).isNotNull();
    assertThat(returnedUser.getId()).isEqualTo(expectedUser.getId());
    assertThat(returnedUser.getEmail()).isEqualTo(expectedUser.getEmail());
    assertThat(returnedUser.getPassword()).isEqualTo(expectedUser.getPassword());
    assertThat(returnedUser.getBalance()).isEqualTo(expectedUser.getBalance());
  }
}