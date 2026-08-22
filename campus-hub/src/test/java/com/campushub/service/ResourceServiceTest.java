package com.campushub.service;

import com.campushub.dto.ResourceResponse;
import com.campushub.entity.Resource;
import com.campushub.entity.User;
import com.campushub.repository.ResourceBookmarkRepository;
import com.campushub.repository.ResourceRepository;
import com.campushub.repository.ResourceUpvoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceUpvoteRepository upvoteRepository;

    @Mock
    private ResourceBookmarkRepository bookmarkRepository;

    @Mock
    private GcsStorageService storageService;

    @InjectMocks
    private ResourceService resourceService;

    private User sampleUser;
    private Resource sampleResource;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .userId(1L)
                .fullName("Alex Rivera")
                .email("alex@campus.edu")
                .build();

        sampleResource = Resource.builder()
                .resourceId(10L)
                .title("Data Structures Notes")
                .description("Complete notes for CS301")
                .subjectCode("CS301")
                .semester(3)
                .category("NOTES")
                .fileGcsUrl("https://storage.googleapis.com/bucket/notes.pdf")
                .uploader(sampleUser)
                .upvotes(5)
                .views(12)
                .build();
    }

    @Test
    void testSearchResourcesReturnsMappedList() {
        when(resourceRepository.searchResources("CS301", 3, "NOTES")).thenReturn(List.of(sampleResource));

        List<ResourceResponse> results = resourceService.searchResources("CS301", 3, "NOTES", sampleUser);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Data Structures Notes", results.get(0).getTitle());
    }

    @Test
    void testIncrementViewCount() {
        when(resourceRepository.findById(10L)).thenReturn(Optional.of(sampleResource));
        when(resourceRepository.save(any(Resource.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResourceResponse response = resourceService.incrementViewCount(10L, sampleUser);

        assertNotNull(response);
        assertEquals(13, response.getViews());
    }
}
