package com.example.proyect_APSTI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class logincontroller {
    @GetMapping("/")
    public String inicio() { return "redirect:/usuarios"; }

    @GetMapping("/login")
    public String mostrarLogin() { return "login"; }
}
