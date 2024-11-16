package org.dungha.blooddonateweb.service;

import org.dungha.blooddonateweb.dto.request.DonationDTO;
import org.dungha.blooddonateweb.model.Donation;
import org.dungha.blooddonateweb.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public Donation getDonationById(int donationId) {
        return donationRepository.findById(donationId).orElse(null);
    }

    public Donation createDonation(DonationDTO donationDto) {
        Donation donation = new Donation();
        donation.setName(donationDto.getName());
        donation.setBirthYear(donationDto.getBirthYear());       // Updated to match "birthYear"
        donation.setGender(donationDto.getGender());             // Updated to match "gender"
        donation.setEmail(donationDto.getEmail());
        donation.setPhone(donationDto.getPhone());               // Updated to match "phone"
        donation.setIdNumber(donationDto.getIdNumber());         // Updated to match "idNumber"
        donation.setCity(donationDto.getCity());                 // Updated to match "city"
        donation.setDistrict(donationDto.getDistrict());         // Updated to match "district"
        donation.setWard(donationDto.getWard());                 // Updated to match "ward"
        donation.setAddress(donationDto.getAddress());           // Updated to match "address"
        donation.setBloodType(donationDto.getBloodType());       // Updated to match "bloodType"
        donation.setVaccineStatus(Boolean.parseBoolean(String.valueOf(donationDto.getVaccineStatus()))); // Updated to match "vaccineStatus"
        donation.setHeight(donationDto.getHeight());
        donation.setWeight(donationDto.getWeight());
        donation.setBmi(donationDto.getBmi());
        donation.setHospitalId(donationDto.getHospitalId());
        return donationRepository.save(donation); // Returns the saved Donation object
    }

    public void updateDonation(Donation donation) {
        donationRepository.save(donation);
    }

    public void deleteDonationById(int donationId) {
        donationRepository.deleteById(donationId);
    }
}