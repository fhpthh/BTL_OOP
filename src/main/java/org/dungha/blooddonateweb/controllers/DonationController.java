package org.dungha.blooddonateweb.controllers;

import org.dungha.blooddonateweb.dto.request.DonationDTO;
import org.dungha.blooddonateweb.dto.response.MessageResponse;
import org.dungha.blooddonateweb.model.Donation;
import lombok.RequiredArgsConstructor;
import org.dungha.blooddonateweb.dto.response.ApiResponse;
import org.dungha.blooddonateweb.model.Hospital;
import org.dungha.blooddonateweb.repository.HospitalRepository;
import org.dungha.blooddonateweb.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donation")
public class DonationController {

    @Autowired
    private final DonationService donationService;
    @Autowired
    private HospitalRepository hospitalRepository;


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDonationById(@PathVariable int id) {
        try {
            // Gọi service để tìm kiếm đơn hiến máu theo id
            Donation donation = donationService.getDonationById(id);
            if (donation == null) {
                // Trường hợp không tìm thấy đơn hiến máu
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("error", "Không tìm thấy đơn hiến máu với ID: " + id));
            }

            // Trả về thông tin đơn hiến máu
            return ResponseEntity.ok(new ApiResponse("success", donation));
        } catch (Exception e) {
            // Xử lý lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<ApiResponse> getDonationsByDonorId(@PathVariable int donorId) {
        try {
            // Gọi service để tìm kiếm các đơn hiến máu theo donorId
            List<Donation> donations = donationService.getDonationsByDonorId(donorId);

            if (donations == null || donations.isEmpty()) {
                // Trường hợp không tìm thấy đơn hiến máu cho donorId
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("error", "Không tìm thấy đơn hiến máu cho donorId: " + donorId));
            }

            // Trả về thông tin các đơn hiến máu của donorId
            return ResponseEntity.ok(new ApiResponse("success", donations));
        } catch (Exception e) {
            // Xử lý lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }

    @GetMapping("/hospital/{userId}")
    public ResponseEntity<ApiResponse> getDonationByUserId(@PathVariable Long userId) {
        try {
            // Tìm bệnh viện theo userId
            Optional<Hospital> hospitalOptional = hospitalRepository.findByUserId(userId);

            // Nếu không tìm thấy bệnh viện
            if (!hospitalOptional.isPresent()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Hospital not found for this user", null));
            }

            Hospital hospital = hospitalOptional.get();
            Long hospitalId = (long) hospital.getId();  // Lấy hospitalId từ bệnh viện

            // Lấy danh sách donations từ hospitalId
            List<Donation> donations = donationService.getDonationsByHospitalId(hospitalId);

            if (donations == null || donations.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No donations found for this hospital", null));
            }

            return ResponseEntity.ok(new ApiResponse("success", donations));

        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PostMapping("/registerdonation")
    public ResponseEntity<?> createDonation(@RequestBody DonationDTO donationDto) {
        try{

            // Kiểm tra các điều kiện hợp lệ khác (ví dụ: chiều cao và cân nặng)
            if (donationDto.getHeight() <= 0 || donationDto.getWeight() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Error: Height and weight must be positive values."));
            }

            // Tạo mới Donation
            Donation donation = new Donation();
            donation.setName(donationDto.getName());
            donation.setBirthYear(donationDto.getBirthYear());
            donation.setGender(donationDto.getGender());
            donation.setEmail(donationDto.getEmail());
            donation.setPhone(donationDto.getPhone());
            donation.setIdNumber(donationDto.getIdNumber());
            donation.setCity(donationDto.getCity());
            donation.setDistrict(donationDto.getDistrict());
            donation.setWard(donationDto.getWard());
            donation.setAddress(donationDto.getAddress());
            donation.setBloodType(donationDto.getBloodType());
            donation.setVaccineStatus(donationDto.getVaccineStatus());
            donation.setHeight(donationDto.getHeight());
            donation.setWeight(donationDto.getWeight());
            donation.setBmi(donationDto.getBmi());
            donation.setHospitalId(donationDto.getHospitalId());
            donation.setDonorId(donationDto.getDonorId());
            donation.setStatus(donationDto.getStatus());

            // Lưu Donation vào cơ sở dữ liệu
            Donation savedDonation = donationService.createDonation(donationDto);

            // Trả về phản hồi thành công
            return ResponseEntity.ok(new MessageResponse("Donation created successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/update-status/{donationId}")
    public ResponseEntity<ApiResponse> updateDonationStatus(@PathVariable int donationId) {
        try {
            // Find the donation by ID
            Donation donation = donationService.getDonationById(donationId);

            if (donation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("error", "Donation not found with ID: " + donationId));
            }

            // Update donation status to "Đã duyệt"
            donation.setStatus("Đã duyệt");

            // Save the updated donation to the database
            donationService.updateDonation(donation);

            // Return a success response
            return ResponseEntity.ok(new ApiResponse("success", "Donation status updated to 'Đã duyệt'"));

        } catch (Exception e) {
            // Handle any error that occurs during the process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Error occurred while updating donation status: " + e.getMessage()));
        }
    }
}