package com.campushub.controller;

import com.campushub.dto.StudyGroupRequest;
import com.campushub.dto.StudyGroupResponse;
import com.campushub.entity.User;
import com.campushub.service.AuthService;
import com.campushub.service.StudyGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "Peer Study Groups", description = "Endpoints for discovering, creating, and joining peer study groups")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;
    private final AuthService authService;

    @GetMapping
    @Operation(summary = "Get active study groups", description = "Retrieve all peer study groups optionally filtered by subject keyword")
    public ResponseEntity<List<StudyGroupResponse>> getAllGroups(@RequestParam(required = false) String subject) {
        return ResponseEntity.ok(studyGroupService.getAllGroups(subject));
    }

    @PostMapping
    public ResponseEntity<StudyGroupResponse> createGroup(
            @Valid @RequestBody StudyGroupRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(studyGroupService.createGroup(request, currentUser));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<StudyGroupResponse> joinGroup(
            @PathVariable("id") Long groupId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(studyGroupService.joinGroup(groupId, currentUser));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<StudyGroupResponse> leaveGroup(
            @PathVariable("id") Long groupId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(studyGroupService.leaveGroup(groupId, currentUser));
    }
}
