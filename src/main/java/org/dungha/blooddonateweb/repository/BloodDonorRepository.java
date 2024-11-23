package org.dungha.blooddonateweb.repository;

import org.dungha.blooddonateweb.model.BloodDonors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodDonorRepository extends JpaRepository<BloodDonors, Integer> {
    Optional<BloodDonors> findByUserId(Long userId);
}