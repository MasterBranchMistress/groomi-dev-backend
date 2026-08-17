package org.groomi.groomidevbackend.auth.fixtures;

import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;
import org.groomi.groomidevbackend.user.UserProfile;

import java.util.UUID;

public class TestUser {
    public static UserProfile isValidUser(){
        return new UserProfile(
                "Jimmie",
                "Smith",
                "777-777-7777",
                "jimmie@example.com",
                false,
                "test-password",
                AuthProvider.LOCAL
        );
    }
}
