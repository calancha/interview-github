package com.orb.interview.infrastructure.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.orb.interview.infrastructure.dtos.AuthenticationToken;

@Repository
public interface AuthenticationTokenRepository extends CrudRepository<AuthenticationToken, String> {
  Optional<AuthenticationToken> findFirstByUserId(String userId);
}
