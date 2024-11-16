package org.dungha.blooddonateweb.service;


import org.dungha.blooddonateweb.dto.response.SignUpDto;
import org.dungha.blooddonateweb.model.User;
import org.dungha.blooddonateweb.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(SignUpDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new RuntimeException("Email is already taken.");
        }

        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setRole(signUpDto.getRole());

        userRepository.save(user);
    }
}
