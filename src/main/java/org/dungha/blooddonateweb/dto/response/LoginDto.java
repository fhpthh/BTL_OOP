package org.dungha.blooddonateweb.dto.response;

import lombok.Data;

@Data
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}