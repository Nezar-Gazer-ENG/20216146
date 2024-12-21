package com.sdaproject.api20216146.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "<html><body><h1>Welcome to the Travel Booking App</h1></body></html>";
    }
}
