package org.groomi.groomidevbackend.shared_packages.exception_handling;

import java.time.LocalDateTime;

public record ErrorResponse(int statusCode, String message, LocalDateTime timestamp) {}

