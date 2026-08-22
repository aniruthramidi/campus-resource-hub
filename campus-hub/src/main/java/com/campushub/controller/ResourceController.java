package com.campushub.controller;

import com.campushub.dto.ResourceRequest;
import com.campushub.dto.ResourceResponse;
import com.campushub.entity.User;
import com.campushub.service.AuthService;
import com.campushub.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
@Tag(name = "Academic Resources", description = "Endpoints for searching, uploading, upvoting, and bookmarking study materials")
public class ResourceController {

    private final ResourceService resourceService;
    private final AuthService authService;

    @GetMapping
    @Operation(summary = "Search academic resources", description = "Filter resources by title, subject code, semester, or category")
    public ResponseEntity<List<ResourceResponse>> searchResources(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String category,
            Authentication authentication) {

        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = authService.getUserByEmail(authentication.getName());
        }

        return ResponseEntity.ok(resourceService.searchResources(query, semester, category, currentUser));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<ResourceResponse>> getTrendingResources(Authentication authentication) {
        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = authService.getUserByEmail(authentication.getName());
        }
        return ResponseEntity.ok(resourceService.getTrendingResources(currentUser));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceResponse> createResource(
            @Valid @RequestPart("data") ResourceRequest request,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) throws IOException {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(resourceService.createResource(request, file, currentUser));
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<ResourceResponse> toggleUpvote(
            @PathVariable("id") Long resourceId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(resourceService.toggleUpvote(resourceId, currentUser));
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<ResourceResponse> toggleBookmark(
            @PathVariable("id") Long resourceId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(resourceService.toggleBookmark(resourceId, currentUser));
    }

    @PostMapping("/{id}/view")
    @Operation(summary = "Increment resource view count", description = "Increments the view count for a specific academic resource")
    public ResponseEntity<ResourceResponse> incrementViewCount(
            @PathVariable("id") Long resourceId,
            Authentication authentication) {

        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = authService.getUserByEmail(authentication.getName());
        }
        return ResponseEntity.ok(resourceService.incrementViewCount(resourceId, currentUser));
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<List<ResourceResponse>> getBookmarks(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(resourceService.getBookmarkedResources(currentUser));
    }
}
