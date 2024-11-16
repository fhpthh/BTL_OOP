package org.dungha.blooddonateweb.service;

import org.dungha.blooddonateweb.model.Hospital;
import org.dungha.blooddonateweb.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository repo;

    //Get danh sach tat ca benh vien
    public List<Hospital> getAllHospitals() {
        return repo.findAll();
    }

    //Get danh sach benh vien theo id
    public Hospital getHospitalsById(int id) {
        return repo.findById(id).get();
    }

    //Them benh vien vao
    public Hospital addHospital(Hospital hospital, MultipartFile imageFile) throws IOException {
        return repo.save(hospital);
    }
    //update thong tin
    public Hospital updateHospital(int id, Hospital hospital, MultipartFile imageFile) throws IOException {
        return repo.save(hospital);
    }

    //delete thong tin
    public void deleteHospital(int id) {
        repo.deleteById(id);
    }

}