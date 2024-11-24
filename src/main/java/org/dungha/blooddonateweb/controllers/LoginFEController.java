package org.dungha.blooddonateweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class LoginFEController {
    @RequestMapping("/login")
    public String login() {
        return "index";
    }
}
