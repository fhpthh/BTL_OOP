package org.dungha.blooddonateweb.repository;

import org.dungha.blooddonateweb.model.BloodRequest;
import org.dungha.blooddonateweb.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Integer> {
    List<BloodRequest> findByHospitalId(int hospitalId);
}