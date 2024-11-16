package org.dungha.blooddonateweb.service;

import org.dungha.blooddonateweb.model.BloodDonors;
import org.dungha.blooddonateweb.repository.BloodDonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BloodDonorService {

    @Autowired
    private BloodDonorRepository bloodDonorRepository;

    public Optional<BloodDonors> getDonorProfile(int id) {
        return bloodDonorRepository.findById(id);
    }

    public Optional<BloodDonors> updateDonorProfile(BloodDonors donor) {
        Optional<BloodDonors> existingDonor = bloodDonorRepository.findById(donor.getId());
        if (existingDonor.isPresent()) {
            BloodDonors updatedDonor = existingDonor.get();
            updatedDonor.setFirstname(donor.getFirstname());
            updatedDonor.setLastname(donor.getLastname());
            updatedDonor.setSex(donor.getSex());
            updatedDonor.setPhone(donor.getPhone());
            updatedDonor.setBloodGroup(donor.getBloodGroup());
            updatedDonor.setBirthday(donor.getBirthday());
            updatedDonor.setAddress(donor.getAddress());
            updatedDonor.setEmail(donor.getEmail());
            bloodDonorRepository.save(updatedDonor);
            return Optional.of(updatedDonor);
        } else {
            return Optional.empty();
        }
    }
}