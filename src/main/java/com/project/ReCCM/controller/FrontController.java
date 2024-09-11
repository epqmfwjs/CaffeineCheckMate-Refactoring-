package com.project.ReCCM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
    @GetMapping("/")
    public String home(){
        return "main";
    }

    @GetMapping("/product")
    public String coffeeList(){
        return "coffeeList";
    }

    @GetMapping("/custom")
    public String custom(){
        return "custom";
    }
}
