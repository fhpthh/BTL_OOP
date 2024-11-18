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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/**") // Bỏ qua CSRF cho API auth
                )
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**", "/webjars/**", "/static/**", "/public/**").permitAll()  // Cho phép tất cả các tài nguyên tĩnh
                        .requestMatchers("/home", "/").permitAll()  // Cho phép trang chủ và form đăng ký
                        .requestMatchers("/api/auth/signin", "/api/auth/signup", "/api/all_hospitals").permitAll()  // Các trang đăng nhập, đăng ký không cần xác thực
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**", "/webjars/**", "/static/**", "/public/**").permitAll()
                        .requestMatchers("/home/donor","/home/hospital","/home", "/").permitAll()
                        .requestMatchers("/api/auth/signin", "/api/auth/signup", "/api/all_hospitals").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            boolean isUser = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));
            boolean isHospital = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_HOSPITAL"));

            if (isAdmin) {
                response.sendRedirect("/admin/home");
            } else if (isUser) {
                response.sendRedirect("/home/donor");
            } else if (isHospital) {
                response.sendRedirect("/home/hospital");
            } else {
                response.sendRedirect("/home");
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, String> result = new HashMap<>();
            result.put("error", "Login failed. Please check your credentials.");
            result.put("message", exception.getMessage());

            PrintWriter writer = response.getWriter();
            writer.write(new ObjectMapper().writeValueAsString(result));
            writer.flush();
        };
    }
}