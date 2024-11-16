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

    @Column(name = "sex")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "is_covid")
    private boolean vaccineStatus;

    @Column(name = "number_phone")
    private String phone;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "address")
    private String address;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "birth_year")
    private String birthYear;

    // New fields added
    @Column(name = "height_cm")
    private double height; // Height in cm

    @Column(name = "weight_kg")
    private double weight; // Weight in kg

    @Column(name = "bmi")
    private double bmi; // BMI (calculated from height and weight)

    @Column(name = "hospital_id")
    private int hospitalId; // ID of the hospital
}
