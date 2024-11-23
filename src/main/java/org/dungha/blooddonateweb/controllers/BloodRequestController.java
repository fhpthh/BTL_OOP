package org.dungha.blooddonateweb.controllers;

import org.dungha.blooddonateweb.dto.request.RequestDTO;
import org.dungha.blooddonateweb.dto.response.ApiResponse;
import org.dungha.blooddonateweb.dto.response.MessageResponse;
import org.dungha.blooddonateweb.model.BloodRequest;
import org.dungha.blooddonateweb.service.BloodRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/request")
public class BloodRequestController {
    @Autowired
    private final BloodRequestService bloodRequestService;

    public BloodRequestController(BloodRequestService bloodRequestService) {
        this.bloodRequestService = bloodRequestService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllBloodRequests() {
        ArrayList<BloodRequest> requests = bloodRequestService.getAllBloodRequests();
        return ResponseEntity.ok(new ApiResponse("Success", requests));
    }

    @GetMapping("/{hospitalId}")
    public ResponseEntity<ApiResponse> getBloodRequestsByHospitalId(@PathVariable int hospitalId) {
        List<BloodRequest> requests = bloodRequestService.getAllBloodRequestsByHospitalId(hospitalId);
        return ResponseEntity.ok(new ApiResponse("Success", requests));
    }

    @PostMapping("/regis")
    public ResponseEntity<ApiResponse> createBloodRequest(@RequestBody BloodRequest bloodRequest) {
        BloodRequest createdRequest = bloodRequestService.createBloodRequest(bloodRequest);
        return ResponseEntity.ok(new ApiResponse("Blood request created", createdRequest));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteBloodRequestById(@PathVariable int id) {
        bloodRequestService.deleteBloodRequestById(id);
        return ResponseEntity.ok(new ApiResponse("Blood request deleted", null));
    }
}