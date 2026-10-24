package org.groomi.groomidevbackend.metadata.pet_owners;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "owners")
public class PetOwner {

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

    @Column
    private String ownerPhotoUrl;

   @Column(nullable = false)
   private UUID groomerId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;



    protected PetOwner() {}
    public PetOwner(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String ownerPhotoUrl,
            UUID groomerId
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ownerPhotoUrl =  ownerPhotoUrl;
        this.groomerId =  groomerId;
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
