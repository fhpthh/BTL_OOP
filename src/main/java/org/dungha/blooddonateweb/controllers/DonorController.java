package org.dungha.blooddonateweb.controllers;


import org.dungha.blooddonateweb.model.BloodDonors;
import org.dungha.blooddonateweb.service.BloodDonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/donor")
public class DonorController {

    @Autowired
    private BloodDonorService bloodDonorService;

    @GetMapping(path = "/profile/{id}")
    public ResponseEntity<BloodDonors> donorProfile(@PathVariable("id") int id) {
        Optional<BloodDonors> donor = bloodDonorService.getDonorProfile(id);
        if (donor.isPresent()) {
            return ResponseEntity.ok(donor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/profile/edit")
    public ResponseEntity<BloodDonors> updateDonorProfile(@RequestBody BloodDonors donor) {
        Optional<BloodDonors> updatedDonor = bloodDonorService.updateDonorProfile(donor);
        if (updatedDonor.isPresent()) {
            return ResponseEntity.ok(updatedDonor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}