package org.dungha.blooddonateweb.models;

import jakarta.persistence.*;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getTotalBloodDonated() {
        return totalBloodDonated;
    }

    public void setTotalBloodDonated(int totalBloodDonated) {
        this.totalBloodDonated = totalBloodDonated;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
