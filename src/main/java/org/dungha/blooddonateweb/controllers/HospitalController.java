package org.dungha.blooddonateweb.controllers;

import org.dungha.blooddonateweb.model.BloodDonors;
import org.dungha.blooddonateweb.model.Hospital;
import org.dungha.blooddonateweb.repository.HospitalRepository;
import org.dungha.blooddonateweb.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HospitalController {

    @Autowired
    private HospitalService service;

    @Autowired
    private HospitalRepository hospitalRepository;

    //Thong tin tat ca benh vien
    @GetMapping("/all_hospitals")
    public ResponseEntity<List<Hospital>> getAllHospitals(){
        return new ResponseEntity<>(service.getAllHospitals(), HttpStatus.OK);
    }
    //Thong tin benh vien theo Id
    @GetMapping("/hospitals/{userId}")
    public ResponseEntity<Hospital> getHospitalProfile(@PathVariable Long userId) {
        try {
            Hospital hospital = service.getHospitalByUserId(userId);
            return ResponseEntity.ok(hospital);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/hospital/{id}")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable int id) {
        try {
            // Lấy thông tin bệnh viện từ service
            Hospital hospital = service.getHospitalById(id);
            if (hospital != null) {
                return ResponseEntity.ok(hospital);
            } else {
                // Trường hợp không tìm thấy bệnh viện
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            // Trường hợp có lỗi trong quá trình xử lý
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/hospitals")
    public ResponseEntity<?> addHospital(@RequestPart Hospital hospital, @RequestPart MultipartFile imageFile){
        try{
            Hospital hospital1 = service.addHospital(hospital, imageFile);
            return new ResponseEntity<>(hospital1, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/hospitals/{userId}/edit")
    public ResponseEntity<Hospital> updateHospitalProfile(@PathVariable String userId, @RequestBody Hospital hospital) {
        Optional<Hospital> updatedHospital = service.updateHospitalProfile(userId, hospital);
        if (updatedHospital.isPresent()) {
            return ResponseEntity.ok(updatedHospital.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/hospitals/{id}")
    public ResponseEntity<String> deleteHospital(@PathVariable Long userId){
        Hospital hospital = service.getHospitalByUserId(userId);
        if(hospital != null){
            service.deleteHospital(Math.toIntExact(userId));
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Failed to deleted", HttpStatus.NOT_FOUND);
    }
}