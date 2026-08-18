package com.campushub.service;

import com.campushub.dto.ResourceRequest;
import com.campushub.dto.ResourceResponse;
import com.campushub.entity.Resource;
import com.campushub.entity.ResourceBookmark;
import com.campushub.entity.ResourceUpvote;
import com.campushub.entity.User;
import com.campushub.repository.ResourceBookmarkRepository;
import com.campushub.repository.ResourceRepository;
import com.campushub.repository.ResourceUpvoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceUpvoteRepository upvoteRepository;
    private final ResourceBookmarkRepository bookmarkRepository;
    private final GcsStorageService storageService;

    @Transactional(readOnly = true)
    public List<ResourceResponse> searchResources(String query, Integer semester, String category, User currentUser) {
        List<Resource> resources = resourceRepository.searchResources(query, semester, category);
        return resources.stream()
                .map(r -> mapToResponse(r, currentUser))
                .collect(Collectors.toList());
    }

    @Transactional
    public ResourceResponse createResource(ResourceRequest request, MultipartFile file, User currentUser) throws IOException {
        String fileGcsUrl = storageService.uploadFile(file);

        Resource resource = Resource.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .subjectCode(request.getSubjectCode().toUpperCase().trim())
                .semester(request.getSemester())
                .category(request.getCategory().toUpperCase().trim())
                .fileGcsUrl(fileGcsUrl)
                .uploader(currentUser)
                .upvotes(0)
                .build();

        Resource savedResource = resourceRepository.save(resource);
        return mapToResponse(savedResource, currentUser);
    }

    @Transactional
    public ResourceResponse toggleUpvote(Long resourceId, User currentUser) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found with ID: " + resourceId));

        Optional<ResourceUpvote> existingUpvote = upvoteRepository.findByUserAndResource(currentUser, resource);
        if (existingUpvote.isPresent()) {
            upvoteRepository.delete(existingUpvote.get());
            resource.setUpvotes(Math.max(0, resource.getUpvotes() - 1));
        } else {
            ResourceUpvote newUpvote = ResourceUpvote.builder()
                    .user(currentUser)
                    .resource(resource)
                    .build();
            upvoteRepository.save(newUpvote);
            resource.setUpvotes(resource.getUpvotes() + 1);
        }

        Resource updatedResource = resourceRepository.save(resource);
        return mapToResponse(updatedResource, currentUser);
    }

    @Transactional
    public ResourceResponse toggleBookmark(Long resourceId, User currentUser) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found with ID: " + resourceId));

        Optional<ResourceBookmark> existingBookmark = bookmarkRepository.findByUserAndResource(currentUser, resource);
        if (existingBookmark.isPresent()) {
            bookmarkRepository.delete(existingBookmark.get());
        } else {
            ResourceBookmark newBookmark = ResourceBookmark.builder()
                    .user(currentUser)
                    .resource(resource)
                    .build();
            bookmarkRepository.save(newBookmark);
        }

        return mapToResponse(resource, currentUser);
    }

    @Transactional(readOnly = true)
    public List<ResourceResponse> getBookmarkedResources(User currentUser) {
        List<ResourceBookmark> bookmarks = bookmarkRepository.findByUserOrderByCreatedAtDesc(currentUser);
        return bookmarks.stream()
                .map(b -> mapToResponse(b.getResource(), currentUser))
                .collect(Collectors.toList());
    }

    private ResourceResponse mapToResponse(Resource resource, User currentUser) {
        boolean isUpvotedByMe = false;
        boolean isBookmarkedByMe = false;

        if (currentUser != null) {
            isUpvotedByMe = upvoteRepository.existsByUserAndResource(currentUser, resource);
            isBookmarkedByMe = bookmarkRepository.existsByUserAndResource(currentUser, resource);
        }

        return ResourceResponse.builder()
                .resourceId(resource.getResourceId())
                .title(resource.getTitle())
                .description(resource.getDescription())
                .subjectCode(resource.getSubjectCode())
                .semester(resource.getSemester())
                .category(resource.getCategory())
                .fileGcsUrl(resource.getFileGcsUrl())
                .uploaderId(resource.getUploader().getUserId())
                .uploaderName(resource.getUploader().getFullName())
                .upvotes(resource.getUpvotes() != null ? resource.getUpvotes() : 0)
                .isUpvotedByMe(isUpvotedByMe)
                .isBookmarkedByMe(isBookmarkedByMe)
                .createdAt(resource.getCreatedAt())
                .build();
    }
}
