package org.groomi.groomidevbackend.user;

import jakarta.persistence.*;
import lombok.Getter;
import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class UserProfile {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;



    public UserProfile(
            String firstName,
            String lastName,
            String phoneNumber,
            String email,
            String passwordHash,
            AuthProvider provider
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
        this.provider = provider;
        this.createdAt = Instant.now();
    }


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }



    // getters/setters
}