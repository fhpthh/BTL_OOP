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

    public Donation getDonationById(int id) {
        return donationRepository.findById(id).orElse(null);
    }

    public List<Donation> getDonationsByDonorId(int donorId) {
        // Gọi repository hoặc database để tìm các đơn hiến máu theo donorId
        return donationRepository.findByDonorId(donorId);
    }

    public List<Donation> getDonationsByHospitalId(Long hospitalId) {
        // Assuming you have a method in your repository to query donations by hospitalId
        return donationRepository.findByHospitalId(hospitalId);
    }

    public Donation createDonation(DonationDTO donationDto) {
        // Tạo đối tượng Donation mới và gán các giá trị từ DonationDTO
        Donation donation = new Donation();
        donation.setName(donationDto.getName());
        donation.setBirthYear(donationDto.getBirthYear());       // Cập nhật theo "birthYear"
        donation.setGender(donationDto.getGender());             // Cập nhật theo "gender"
        donation.setEmail(donationDto.getEmail());
        donation.setPhone(donationDto.getPhone());               // Cập nhật theo "phone"
        donation.setIdNumber(donationDto.getIdNumber());         // Cập nhật theo "idNumber"
        donation.setCity(donationDto.getCity());                 // Cập nhật theo "city"
        donation.setDistrict(donationDto.getDistrict());         // Cập nhật theo "district"
        donation.setWard(donationDto.getWard());                 // Cập nhật theo "ward"
        donation.setAddress(donationDto.getAddress());           // Cập nhật theo "address"
        donation.setBloodType(donationDto.getBloodType());       // Cập nhật theo "bloodType"
        donation.setVaccineStatus(donationDto.getVaccineStatus());  // Cập nhật theo "vaccineStatus"
        donation.setHeight(donationDto.getHeight());             // Cập nhật theo "height"
        donation.setWeight(donationDto.getWeight());             // Cập nhật theo "weight"
        donation.setBmi(donationDto.getBmi());                   // Cập nhật theo "bmi"
        donation.setHospitalId(donationDto.getHospitalId());     // Cập nhật theo "hospitalId"
        donation.setDonorId(donationDto.getDonorId());
        donation.setStatus(donationDto.getStatus());

        // Lưu donation vào cơ sở dữ liệu
        return donationRepository.save(donation); // Lưu và trả về đối tượng Donation đã lưu
    }

    public void updateDonation(Donation donation) {
        donationRepository.save(donation);
    }

    public void deleteDonationById(int donationId) {
        donationRepository.deleteById(donationId);
    }
}