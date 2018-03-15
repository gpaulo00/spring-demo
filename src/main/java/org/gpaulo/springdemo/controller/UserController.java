package org.gpaulo.springdemo.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import models.User;
import repos.UserRepository;

@RestController
public class UserController {
    @Autowired
    UserRepository repository;

    @RequestMapping("/save")
    public String process(){
        // save a single Customer
        repository.save(new User("Jack", "Smith"));

        // save a list of Customers
        repository.save(Arrays.asList(new User("Adam", "Johnson"), new User("Kim", "Smith"),
                new User("David", "Williams"), new User("Peter", "Davis")));

        return "Done";
    }


    @RequestMapping("/findall")
    public String findAll(){
        String result = "";

        for(User cust : repository.findAll()){
            result += cust.toString() + "<br>";
        }

        return result;
    }

    @RequestMapping("/findbyid")
    public String findById(@RequestParam("id") long id){
        String result = "";
        result = repository.findOne(id).toString();
        return result;
    }

    @RequestMapping("/findbylastname")
    public String fetchDataByLastName(@RequestParam("lastname") String lastName){
        String result = "";

        for(User cust: repository.findByLastName(lastName)){
            result += cust.toString() + "<br>";
        }

        return result;
    }
}