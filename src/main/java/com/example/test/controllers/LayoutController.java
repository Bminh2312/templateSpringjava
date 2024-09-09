package com.example.test.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.prefix}/layout")
public class LayoutController {
    @GetMapping("/admin1")
    public String index1(){
        return "Admin page1";
    }

    @GetMapping("/admin2")
    public String index2(){
        return "Admin page2";
    }
}
