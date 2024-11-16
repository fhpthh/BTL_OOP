package org.dungha.blooddonateweb.repository;

import org.dungha.blooddonateweb.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {

}