package org.groomi.groomidevbackend.dashboard;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserDashboard {

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



    protected UserDashboard() {}
    public UserDashboard(
            String firstName,
            String lastName,
            String email
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}