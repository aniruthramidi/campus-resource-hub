package com.campushub.util;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileValidatorTest {

    @Test
    void testValidPdfFilePassesValidation() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "lecture_notes.pdf", "application/pdf", "Sample PDF Content".getBytes());
        assertDoesNotThrow(() -> FileValidator.validateFile(file));
    }

    @Test
    void testEmptyFileThrowsException() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "empty.pdf", "application/pdf", new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> FileValidator.validateFile(file));
    }

    @Test
    void testUnsupportedExtensionThrowsException() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "executable.exe", "application/octet-stream", "Malicious Data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> FileValidator.validateFile(file));
    }
}
