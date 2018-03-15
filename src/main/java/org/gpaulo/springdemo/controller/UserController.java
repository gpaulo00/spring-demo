package org.gpaulo.springdemo.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.repos.UserRepository;

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

    @RequestMapping("/save")
    public String process() {
        // save a single user
        repository.save(new User("Gustavo", "Paulo"));

        // save a list of Customers
        repository.saveAll(Arrays.asList(
                new User("Jhonny", "Paulo"),
                new User("Estheban", "Paulo"),
                new User("Rossmy", "Segura")
        ));

        return "Done";
    }

    @RequestMapping("/findall")
    public String findAll() {
        StringBuilder result = new StringBuilder();

        for (User u : repository.findAll()) {
            result.append(u.toString()).append("<br>");
        }

        return result.toString();
    }

    @RequestMapping("/findbyid")
    public String findById(@RequestParam("id") long id) {
        return repository.findById(id).toString();
    }

    @RequestMapping("/findbylastname")
    public String fetchDataByLastName(@RequestParam("lastname") String lastName) {
        StringBuilder result = new StringBuilder();

        for (User u : repository.findByLastName(lastName)) {
            result.append(u.toString()).append("<br>");
        }

        return result.toString();
    }
}