package com.campushub.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("Academic resource not found with ID: " + id);
    }
}
