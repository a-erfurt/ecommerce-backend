package org.ecommerce.backend.repository;

import java.util.Optional;

import org.ecommerce.backend.models.enums.ERole;
import org.ecommerce.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
