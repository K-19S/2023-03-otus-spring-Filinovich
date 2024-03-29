package ru.otus.filinovich.dao.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.filinovich.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
