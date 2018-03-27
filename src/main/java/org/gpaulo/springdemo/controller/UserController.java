package org.gpaulo.springdemo.controller;

import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {
    private UserRepository repository;

    /**
     * Setup the UserController
     *
     * @param userRepository UserRepository impl.
     */
    @Autowired
    public UserController(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @PostMapping
    public User insert(@RequestBody User input) {
        // save a single user
        return repository.save(input);
    }

    @GetMapping
    public Iterable<User> search(@RequestParam Optional<String> query) {
        return query
            .map((String q) -> repository.search(q))
            .orElseGet(() -> repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable long id) {
        return repository.findById(id)
            .map((User u) -> {
                repository.delete(u);
                return ResponseEntity.noContent().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateById(@PathVariable long id, @RequestBody User input) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        input.setId(id);
        repository.save(input);
        return ResponseEntity.noContent().build();
    }
}
