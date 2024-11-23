package org.dungha.blooddonateweb.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.dungha.blooddonateweb.dto.response.RedirectResponse;
import org.dungha.blooddonateweb.model.*;
import org.dungha.blooddonateweb.dto.response.LoginDto;
import org.dungha.blooddonateweb.dto.response.SignUpDto;
import org.dungha.blooddonateweb.dto.response.MessageResponse;
import org.dungha.blooddonateweb.repository.BloodDonorRepository;
import org.dungha.blooddonateweb.repository.HospitalRepository;
import org.dungha.blooddonateweb.repository.RoleRepository;
import org.dungha.blooddonateweb.repository.UserRepository;
import org.dungha.blooddonateweb.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    private BloodDonorRepository bloodDonorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private HospitalRepository hospitalRepository;

    // Login method (authenticate)
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            // Thực hiện xác thực người dùng với Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsernameOrEmail(),
                            loginDto.getPassword()
                    )
            );

            // Lưu thông tin xác thực vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // Tạo JWT từ thông tin đăng nhập

            String usernameOrEmail = authentication.getName();
            Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            User user = userOptional.get();

            String jwt = jwtTokenProvider.generateToken(authentication.getName(), authorities);

            List<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            // Tạo response JSON bằng Map
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", jwt);
            response.put("roles", roles);
            response.put("id", user.getId());
            System.out.println("JWT Token: " + jwt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

            // Assign role
            Role role = roleRepository.findByName(RoleName.valueOf("ROLE_" + signUpDto.getRole().toUpperCase()))
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRoles(Collections.singleton(role));

            // Save user to the database
            userRepository.save(user);

            if (role.getName().equals(RoleName.ROLE_USER)) {
                BloodDonors bloodDonors = new BloodDonors();
                bloodDonors.setName(user.getName()); // Set the name from the user
                bloodDonors.setEmail(user.getEmail()); // Set the email from the user
                bloodDonors.setUser(user);
                // Save the BloodDonors entry
                bloodDonorRepository.save(bloodDonors);
            }
            else if(role.getName().equals(RoleName.ROLE_HOSPITAL)) {
                Hospital hospital = new Hospital();
                hospital.setName(user.getName());
                hospital.setEmail(user.getEmail());
                hospital.setUser(user);
                hospitalRepository.save(hospital);
            }
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

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<User> donorProfile(@PathVariable("id") int id) {
        // Lấy người dùng từ repository
        Optional<User> donor = userRepository.findById((long) id);  // Đảm bảo userRepository có phương thức này

        // Kiểm tra xem người dùng có tồn tại hay không
        if (donor.isPresent()) {
            return ResponseEntity.ok(donor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy danh sách roles của user
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(roles);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        try {
            // Xóa thông tin xác thực khỏi SecurityContext
            SecurityContextHolder.clearContext();

            // Hủy phiên làm việc (session) nếu có
            HttpSession session = request.getSession(false); // Trả về null nếu không có session
            if (session != null) {
                session.invalidate(); // Hủy session
            }

            // Trả về phản hồi thành công
            return ResponseEntity.ok(new MessageResponse("Logout thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Có lỗi xảy ra khi logout: " + e.getMessage()));
        }
    }
}