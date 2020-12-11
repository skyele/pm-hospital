package com.sjtupm.manage.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    String test(){
        return "go fuck pm!";
    }
}
