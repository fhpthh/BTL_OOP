package org.dungha.blooddonateweb.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller

public class AdminController {
    @RequestMapping("/profile")
    public String profile() {
        return "admin/index";
    }
}
