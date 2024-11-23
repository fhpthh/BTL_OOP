package org.dungha.blooddonateweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "donation")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String gender;
    private String email;
    private boolean vaccineStatus;
    private String phone;

    private String idNumber;

    private String city;

    private String district;

    private String ward;

    private String address;

    private String bloodType;

    private String birthYear;


    private int height; // Height in cm

    private int weight; // Weight in kg

    private double bmi; // BMI (calculated from height and weight)

    private int hospitalId; // ID of the hospital

    private int donorId;

    private String status;

}
