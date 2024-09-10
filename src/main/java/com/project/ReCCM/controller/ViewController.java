package com.project.ReCCM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/product")
    public String coffeeList(){
        return "coffeeList";
    }
}
