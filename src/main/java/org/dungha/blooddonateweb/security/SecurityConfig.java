package org.dungha.blooddonateweb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/**")  // Bỏ qua CSRF cho các API auth
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**", "/webjars/**", "/static/**", "/public/**").permitAll()  // Cho phép tất cả các tài nguyên tĩnh
                        .requestMatchers("/home", "/", "/register", "/donation/create").permitAll()  // Cho phép trang chủ và form đăng ký
                        .requestMatchers("/api/auth/signin", "/api/auth/signup", "/api/all_hospitals").permitAll()  // Các trang đăng nhập, đăng ký không cần xác thực
                        .anyRequest().authenticated()  // Các yêu cầu khác yêu cầu đăng nhập
                )
                .formLogin(form -> form
                        .loginPage("/") // Trang đăng nhập tùy chỉnh
                        .loginProcessingUrl("/api/auth/signin") // URL xử lý đăng nhập
                        .defaultSuccessUrl("/home", true) // Chuyển hướng về trang chủ sau khi đăng nhập thành công
                        .failureUrl("/login?error=true") // Chuyển hướng về trang login nếu đăng nhập thất bại
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL để logout
                        .logoutSuccessUrl("/home") // Chuyển hướng về trang chủ sau khi logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Kiểm tra quyền của người dùng và chuyển hướng theo vai trò
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            boolean isUser = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));
            boolean isHospital = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_HOSPITAL"));

            if (isAdmin) {
                response.sendRedirect("/admin/home"); // Trang dành cho admin
            } else if (isUser) {
                response.sendRedirect("/user/home"); // Trang dành cho user
            } else if (isHospital) {
                response.sendRedirect("/hospital/dashboard"); // Trang dành cho hospital
            } else {
                response.sendRedirect("/home"); // Trang chủ nếu không có vai trò
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {
            public void onAuthenticationFailure(javax.servlet.http.HttpServletRequest request,
                                                javax.servlet.http.HttpServletResponse response,
                                                org.springframework.security.core.AuthenticationException exception) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                Map<String, String> result = new HashMap<>();
                result.put("error", "Login failed. Please check your credentials.");
                try {
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(result));
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}