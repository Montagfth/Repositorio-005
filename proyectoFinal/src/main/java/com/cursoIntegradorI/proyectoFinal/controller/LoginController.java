package com.cursoIntegradorI.proyectoFinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String mostrarLogin() {
        return "login"; // Busca templates/login.html
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/calendar")
    public String calendar(){return "calendar";}

}
