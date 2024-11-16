package org.dungha.blooddonateweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "blood_donos")
public class BloodDonors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstname;
    private String lastname;
    private String sex;

    @Column(name = "number_phone")
    private String phone;

    @Column(name = "total_blood_donated")
    private int totalBloodDonated;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "birthday_id")
    private int birthday;

    @Column(name = "address")
    private String address;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "email")
    private String email;
}
