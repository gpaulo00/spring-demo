package org.gpaulo.springdemo.controller;

import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User insert(@RequestBody User input) {
        // save a single user
        return repository.save(input);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Iterable<User> findAll() {
        return repository.findAll();
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findById(@PathVariable long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
