package org.dungha.blooddonateweb.controllers.profile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller

public class AdminFEController {
    @RequestMapping("/profile")
    public String profile() {
        return "admin/profile/index";
    }
}
