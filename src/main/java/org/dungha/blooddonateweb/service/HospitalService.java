package org.dungha.blooddonateweb.service;

import org.dungha.blooddonateweb.model.BloodDonors;
import org.dungha.blooddonateweb.model.Hospital;
import org.dungha.blooddonateweb.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository repo;

    //Get danh sach tat ca benh vien
    public List<Hospital> getAllHospitals() {
        return repo.findAll();
    }

    public Hospital getHospitalByUserId(Long userId) {
        return repo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Hospital not found for user id: " + userId));
    }

    public Hospital getHospitalById(int id) {
        // Giả sử bạn đang sử dụng Spring Data JPA
        Optional<Hospital> hospitalOptional = repo.findById(id);
        return hospitalOptional.orElse(null); // Trả về null nếu không tìm thấy
    }

    //Them benh vien vao
    public Hospital addHospital(Hospital hospital, MultipartFile imageFile) throws IOException {
        return repo.save(hospital);
    }
    //update thong tin
    public Optional<Hospital> updateHospitalProfile(String userId, Hospital hospital){
        Optional<Hospital> existingHospital = repo.findByUserId(Long.valueOf(userId));

        if (existingHospital.isPresent()) {
            Hospital hospitalToUpdate = existingHospital.get();

            // Cập nhật các trường cần thay đổi
            hospitalToUpdate.setPhone(hospital.getPhone());
            hospitalToUpdate.setAddress(hospital.getAddress());

            // Lưu lại vào cơ sở dữ liệu
            repo.save(hospitalToUpdate);
            return Optional.of(hospitalToUpdate);
        }

        // Trả về Optional rỗng nếu không tìm thấy
        return Optional.empty();
    }

    //delete thong tin
    public void deleteHospital(int id) {
        repo.deleteById(id);
    }

}