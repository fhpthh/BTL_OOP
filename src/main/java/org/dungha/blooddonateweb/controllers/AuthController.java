package org.dungha.blooddonateweb.controllers;

import org.dungha.blooddonateweb.dto.response.RedirectResponse;
import org.dungha.blooddonateweb.model.Role;
import org.dungha.blooddonateweb.model.RoleName;
import org.dungha.blooddonateweb.model.User;
import org.dungha.blooddonateweb.dto.response.LoginDto;
import org.dungha.blooddonateweb.dto.response.SignUpDto;
import org.dungha.blooddonateweb.dto.response.MessageResponse;
import org.dungha.blooddonateweb.repository.RoleRepository;
import org.dungha.blooddonateweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Login method (authenticate)
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto, HttpSession session) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsernameOrEmail(), loginDto.getPassword()));

            // Store authentication in SecurityContext to manage session
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Lưu thông tin đăng nhập vào session
            session.setAttribute("username", loginDto.getUsernameOrEmail());

            // Trả về phản hồi thành công mà không gây ra chuyển hướng
            return ResponseEntity.ok(new MessageResponse("User signed-in successfully!"));
        } catch (Exception e) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid username or password"));
        }
    }

    // Register new user
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(signUpDto.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Username is already taken!"));
            }

            // Check if email already exists
            if (userRepository.existsByEmail(signUpDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Email is already taken!"));
            }

            // Create new user
            User user = new User();
            user.setName(signUpDto.getName());
            user.setUsername(signUpDto.getUsername());
            user.setEmail(signUpDto.getEmail());
            user.setPassword(signUpDto.getPassword());

            // Assign role
            Role role = roleRepository.findByName(RoleName.valueOf("ROLE_" + signUpDto.getRole().toUpperCase()))
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRoles(Collections.singleton(role));

            // Save user to the database
            userRepository.save(user);

            // Return success message
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        } catch (RuntimeException e) {
            // Handle role not found exception or other runtime issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while registering the user: " + e.getMessage()));
        }
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}