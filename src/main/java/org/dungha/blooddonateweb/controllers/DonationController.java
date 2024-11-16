package org.dungha.blooddonateweb.controllers;

import org.dungha.blooddonateweb.dto.request.DonationDTO;
import org.dungha.blooddonateweb.model.Donation;
import lombok.RequiredArgsConstructor;
import org.dungha.blooddonateweb.dto.response.ApiResponse;
import org.dungha.blooddonateweb.service.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donation")
public class DonationController {

    private final DonationService donationService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getAllDonation(){
        try{
            List<Donation> donations = donationService.getAllDonations();
            return ResponseEntity.ok(new ApiResponse("success", donations));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get/{donationId}")
    public ResponseEntity<ApiResponse> getDonationByID(@PathVariable Long donationId) {
        try{
            Donation donation = donationService.getDonationById(Math.toIntExact(donationId));
            return ResponseEntity.ok(new ApiResponse("success", donation));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createDonation(@ModelAttribute DonationDTO donation) {
        try{
            Donation donationTmp = donationService.createDonation(donation);
            return ResponseEntity.ok(new ApiResponse("success", donationTmp));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}