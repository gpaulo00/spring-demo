package org.gpaulo.springdemo.services;

import java.util.Optional;

import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDAO implements UserService {
  private UserRepository userRepo;

  @Autowired
  public UserDAO(UserRepository userRepo) {
      this.userRepo = userRepo;
  }

  public User save(User u) {
    return userRepo.save(u);
  }
  public Iterable<User> list() {
    return this.list(Optional.empty());
  }
  public Iterable<User> list(Optional<String> query) {
    return query.map((String q) -> userRepo.search(q)).orElseGet(() -> userRepo.findAll());
  }
  public Optional<User> get(long id) {
    return userRepo.findById(id);
  }
  public Optional<User> update(long id, User u) {
    if (!userRepo.existsById(id)) {
      return Optional.empty();
    }
    u.setId(id);
    return Optional.of(userRepo.save(u));
  }

  public boolean delete(long id) {
    if (!userRepo.existsById(id)) {
      return false;
    }
    userRepo.deleteById(id);
    return true;
  }
}
