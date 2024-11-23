package org.dungha.blooddonateweb.dto.request;

import lombok.Data;

@Data
public class RequestDTO {
    private int hospitalId;
    private String bloodGroupId;
    private int quantityRequested;
}
