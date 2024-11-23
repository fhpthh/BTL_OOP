package org.dungha.blooddonateweb.repository;

import org.dungha.blooddonateweb.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Integer>{
    List<Donation> findByHospitalId(Long hospitalId);
    List<Donation> findByDonorId(int donorId);
}