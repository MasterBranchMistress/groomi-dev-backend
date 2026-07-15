package org.groomi.groomidevbackend.shared_packages.api_response;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ApiResponse<T> {

    private String message;
    private T data;
    private Instant timestamp;


    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }


}
