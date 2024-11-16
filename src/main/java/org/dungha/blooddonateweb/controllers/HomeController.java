package org.dungha.blooddonateweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home") // Base path for this controller
public class HomeController {

    // Mapping for donor home page
    @RequestMapping("/donor")
    public String homeForDonor() {
        return "admin/home/index"; // View for donors
    }

    // Mapping for hospital home page
    @RequestMapping("/hospital")
    public String homeForHospital() {
        return "admin/home/index2"; // View for hospitals
    }
}
