package org.dungha.blooddonateweb.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class DonorDTO implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String createdAt;
    private String updatedAt;
}