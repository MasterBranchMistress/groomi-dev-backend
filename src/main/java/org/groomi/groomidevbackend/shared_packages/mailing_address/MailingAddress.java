package org.groomi.groomidevbackend.shared_packages.mailing_address;

import jakarta.persistence.Embeddable;

@Embeddable
public class MailingAddress {

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public MailingAddress() {
    }

    public MailingAddress(
            String street,
            String city,
            String state,
            String zipCode,
            String country
    ) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    // getters/setters
}