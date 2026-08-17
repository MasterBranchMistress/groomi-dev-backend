package org.groomi.groomidevbackend.user;

import jakarta.persistence.*;
import lombok.Getter;
import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;
import software.amazon.awssdk.services.sesv2.endpoints.internal.Value;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class UserProfile {

    @Id
    @GeneratedValue
    private UUID id;

    public UUID getId() {
        return id;
    }

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    public String getEmail(){
        return email;
    }

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Boolean emailVerified;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;



    protected  UserProfile() {}
    public UserProfile(
            String firstName,
            String lastName,
            String phoneNumber,
            String email,
            Boolean emailVerified,
            String passwordHash,
            AuthProvider provider
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emailVerified =  emailVerified;
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