package org.dungha.blooddonateweb.controllers;


import org.dungha.blooddonateweb.model.BloodDonors;
import org.dungha.blooddonateweb.repository.BloodDonorRepository;
import org.dungha.blooddonateweb.service.BloodDonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DonorController {

    @Autowired
    private BloodDonorService bloodDonorService;

    @Autowired
    private BloodDonorRepository bloodDonorsRepository;

    @GetMapping("/donor/{userId}")
    public ResponseEntity<BloodDonors> getBloodDonorProfile(@PathVariable Long userId) {
        // Tìm thông tin người hiến máu theo user_id
        Optional<BloodDonors> donorOpt = bloodDonorsRepository.findByUserId(userId);

        if (donorOpt.isPresent()) {
            return ResponseEntity.ok(donorOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Trả về lỗi nếu không tìm thấy
        }
    }

    @PutMapping(path = "/donor/{userId}/edit")
    public ResponseEntity<BloodDonors> updateDonorProfile(
            @PathVariable Long userId,
            @RequestBody BloodDonors donor) {
        Optional<BloodDonors> updatedDonor = bloodDonorService.updateDonorProfile(userId, donor);
        if (updatedDonor.isPresent()) {
            return ResponseEntity.ok(updatedDonor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}