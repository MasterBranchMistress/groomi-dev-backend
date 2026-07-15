package org.groomi.groomidevbackend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserProfile, UUID> {

    boolean existsByEmail(String email);

}