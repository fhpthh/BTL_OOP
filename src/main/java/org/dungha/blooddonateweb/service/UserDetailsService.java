package org.dungha.blooddonateweb.service;

import org.dungha.blooddonateweb.dto.response.SignUpDto;
import org.dungha.blooddonateweb.model.BloodDonors;
import org.dungha.blooddonateweb.model.User;
import org.dungha.blooddonateweb.repository.BloodDonorRepository;
import org.dungha.blooddonateweb.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final BloodDonorRepository bloodDonorRepository; // Inject BloodDonorRepository

    public UserDetailsService(UserRepository userRepository, BloodDonorRepository bloodDonorRepository) {
        this.userRepository = userRepository;
        this.bloodDonorRepository = bloodDonorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
    }

    public User registerUser(SignUpDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new RuntimeException("Email is already taken.");
        }

        // Tạo người dùng mới
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setPassword(signUpDto.getPassword());
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setRole(signUpDto.getRole()); // Giả sử bạn có phương thức setRole

        // Lưu người dùng vào cơ sở dữ liệu
        User savedUser = userRepository.save(user);

        // Trả về người dùng đã lưu
        return savedUser;
    }
}
