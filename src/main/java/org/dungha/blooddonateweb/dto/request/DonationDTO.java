package org.dungha.blooddonateweb.dto.request;

import lombok.Data;

@Data
public class DonationDTO {
    private String name;
    private String birthYear;
    private String gender;
    private String email;
    private String phone;
    private String idNumber;
    private String city;
    private String district;
    private String ward;
    private String address;
    private String bloodType;
    private Boolean vaccineStatus;
    private int height;
    private int weight;
    private double bmi;
    private int hospitalId;
    private int donorId;
    private String status;
}