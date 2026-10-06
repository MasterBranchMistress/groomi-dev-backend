package org.groomi.groomidevbackend.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDashboardRepository extends JpaRepository<UserDashboard, UUID> {

    boolean existsByEmail(String email);
    Optional<UserDashboard> findByEmail(String email);

}