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

    private String name;  // Tên đầy đủ người hiến máu (lấy từ bảng User)

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

    @Column(name = "email")
    private String email;



    // Quan hệ với User (Many-to-One)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Transient
    private String username; // Tạm thời lưu trữ username nếu cần

    @PostLoad
    public void loadUserDetails() {
        if (user != null) {
            this.name = user.getName(); // Gán tên từ bảng User vào name
            this.email = user.getEmail(); // Lấy email từ User vào BloodDonors (nếu cần)
            this.username = user.getUsername(); // Lấy username từ User vào BloodDonors (nếu cần)
        }
    }
}
