package org.dungha.blooddonateweb.controllers;

import org.dungha.blooddonateweb.model.Hospital;
import org.dungha.blooddonateweb.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HospitalController {

    @Autowired
    private HospitalService service;

    //Thong tin tat ca benh vien
    @GetMapping("/all_hospitals")
    public ResponseEntity<List<Hospital>> getAllHospitals(){
        return new ResponseEntity<>(service.getAllHospitals(), HttpStatus.OK);
    }
    //Thong tin benh vien theo Id
    @GetMapping("/hospitals/{id}")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable int id){

        Hospital hospital = service.getHospitalsById(id);
        if(hospital != null)
            return new ResponseEntity<>(hospital, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @PutMapping("/hospitals/{id}")
    public ResponseEntity<String> updateHospital(@PathVariable int id, @RequestBody Hospital hospital, @RequestPart MultipartFile imageFile){
        Hospital hospital1 = null;
        try{
            hospital1 = service.updateHospital(id, hospital, imageFile);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if(hospital1 != null){
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Failed to updated", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/hospitals/{id}")
    public ResponseEntity<String> deleteHospital(@PathVariable int id){
        Hospital hospital = service.getHospitalsById(id);
        if(hospital != null){
            service.deleteHospital(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Failed to deleted", HttpStatus.NOT_FOUND);
    }
}