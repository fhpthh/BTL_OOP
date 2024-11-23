package org.dungha.blooddonateweb.controllers;

import org.dungha.blooddonateweb.model.Role;
import org.dungha.blooddonateweb.model.User;
import org.dungha.blooddonateweb.repository.RoleRepository;
import org.dungha.blooddonateweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/user_roles")
public class UserRolesController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Lấy vai trò của người dùng
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, String>> getUserRole(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Set<Role> roles = user.getRoles();

            // Kiểm tra các vai trò của người dùng
            for (Role role : roles) {
                if (role.getName().equals("ROLE_USER")) {
                    Map<String, String> response = new HashMap<>();
                    response.put("role", "ROLE_USER");
                    return ResponseEntity.ok(response); // Trả về vai trò ROLE_USER
                }
                if (role.getName().equals("ROLE_HOSPITAL")) {
                    Map<String, String> response = new HashMap<>();
                    response.put("role", "ROLE_HOSPITAL");
                    return ResponseEntity.ok(response); // Trả về vai trò ROLE_HOSPITAL
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("role", "not authorized"));
    }
}