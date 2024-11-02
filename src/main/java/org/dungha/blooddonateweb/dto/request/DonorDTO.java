package org.dungha.blooddonateweb.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class DonorDTO implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String createdAt;
    private String updatedAt;
}