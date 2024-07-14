package com.drgproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/navigation_bar")
public class NavigationBarController {
    @GetMapping()
    public String navigationBar(){
        return "navigation_bar";
    }
}
