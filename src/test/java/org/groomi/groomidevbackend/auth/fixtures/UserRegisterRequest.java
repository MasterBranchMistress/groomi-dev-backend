package org.groomi.groomidevbackend.auth.fixtures;

import org.groomi.groomidevbackend.auth.dto.register.RegisterRequest;

public class UserRegisterRequest {
    public static RegisterRequest isValidRegisterRequest(){
        RegisterRequest request =  new RegisterRequest();
        request.setFirstName("test");
        request.setLastName("user");
        request.setEmail("test-email@example.com");
        request.setPhoneNumber("777-777-7777");
        request.setPassword("Password123!");
        return request;
    }
    public static RegisterRequest isExistingUserAlready(){
        RegisterRequest request =  new RegisterRequest();
        request.setFirstName("Jimmie");
        request.setLastName("Smith");
        request.setEmail("jimmie@example.com");
        request.setPhoneNumber("777-777-7777");
        request.setPassword("Password123!");
        return request;
    }
}
