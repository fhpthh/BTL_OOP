package org.dungha.blooddonateweb.repository;

import org.dungha.blooddonateweb.models.BloodDonors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodDonorRepository extends JpaRepository<BloodDonors, Integer> {
    // You can define custom queries if necessary
}
