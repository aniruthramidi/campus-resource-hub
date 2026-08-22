package com.campushub.exception;

public class StudyGroupNotFoundException extends RuntimeException {
    public StudyGroupNotFoundException(Long id) {
        super("Peer study group not found with ID: " + id);
    }
}
