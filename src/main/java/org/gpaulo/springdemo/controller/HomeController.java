package org.gpaulo.springdemo.controller;

import org.gpaulo.springdemo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private UserRepository userRepo;

    @Autowired
    public HomeController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @RequestMapping("/")
    public String welcome(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "welcome";
    }

    @RequestMapping("/search")
    public String search(Model model, @RequestParam String query) {
        model.addAttribute("users", userRepo.search(query));
        return "welcome";
    }
}
