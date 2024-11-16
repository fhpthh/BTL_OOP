package org.dungha.blooddonateweb.repository;

import org.dungha.blooddonateweb.model.Role;
import org.dungha.blooddonateweb.model.RoleName;  // Thêm import enum RoleName
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);  // Thay đổi kiểu tham số từ String sang RoleName
}