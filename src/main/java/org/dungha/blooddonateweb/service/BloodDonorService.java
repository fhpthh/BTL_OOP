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

    public Optional<BloodDonors> updateDonorProfile(Long userId, BloodDonors donor) {
        // Kiểm tra xem donor có tồn tại không
        Optional<BloodDonors> existingDonor = bloodDonorRepository.findByUserId(userId);

        if (existingDonor.isPresent()) {
            BloodDonors donorToUpdate = existingDonor.get();

            donorToUpdate.setSex(donor.getSex());
            donorToUpdate.setPhone(donor.getPhone());
            donorToUpdate.setAddress(donor.getAddress());

            // Lưu lại vào cơ sở dữ liệu
            bloodDonorRepository.save(donorToUpdate);
            return Optional.of(donorToUpdate);
        }

        // Trả về Optional rỗng nếu không tìm thấy
        return Optional.empty();
    }
}