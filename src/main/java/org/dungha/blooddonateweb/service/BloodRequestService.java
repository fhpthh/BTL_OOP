package org.dungha.blooddonateweb.service;

import org.dungha.blooddonateweb.dto.request.RequestDTO;
import org.dungha.blooddonateweb.model.BloodRequest;
import org.dungha.blooddonateweb.repository.BloodRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    public BloodRequest createBloodRequest(BloodRequest bloodRequestDto) {
        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setBloodGroupId(bloodRequestDto.getBloodGroupId());
        bloodRequest.setQuantityRequested(bloodRequestDto.getQuantityRequested());
        bloodRequest.setHospitalId(bloodRequestDto.getHospitalId());
        return bloodRequestRepository.save(bloodRequest);  // Lưu vào DB
    }

    public ArrayList<BloodRequest> getAllBloodRequests() {
        return (ArrayList<BloodRequest>) bloodRequestRepository.findAll();
    }

    public List<BloodRequest> getAllBloodRequestsByHospitalId(int hospitalId) {
        return bloodRequestRepository.findByHospitalId(hospitalId);
    }

    public void deleteBloodRequestById(int id) {
        if (bloodRequestRepository.existsById(id)) {
            bloodRequestRepository.deleteById(id);
        } else {
            throw new RuntimeException("Blood request not found with ID: " + id);
        }
    }
}
