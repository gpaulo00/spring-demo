package org.gpaulo.springdemo.controllers;

import java.util.Optional;
import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {
  private UserService userService;

  /**
   * Setup the UserController
   *
   * @param userService UserService
   */
  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public User insert(@RequestBody User input) {
    // save a single user
    return userService.save(input);
  }

  @GetMapping
  public Iterable<User> search(@RequestParam Optional<String> query) {
    return userService.list(query);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable long id) {
    return userService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteById(@PathVariable long id) {
    if (userService.delete(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateById(@PathVariable long id, @RequestBody User input) {
    return userService
      .update(id, input)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
