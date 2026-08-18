package com.campushub.service;

import com.campushub.dto.StudyGroupRequest;
import com.campushub.dto.StudyGroupResponse;
import com.campushub.entity.StudyGroup;
import com.campushub.entity.User;
import com.campushub.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;

    @Transactional(readOnly = true)
    public List<StudyGroupResponse> getAllGroups(String subject) {
        List<StudyGroup> groups;
        if (subject != null && !subject.isBlank()) {
            groups = studyGroupRepository.findBySubjectContainingIgnoreCaseOrderByCreatedAtDesc(subject);
        } else {
            groups = studyGroupRepository.findAllByOrderByCreatedAtDesc();
        }

        return groups.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudyGroupResponse createGroup(StudyGroupRequest request, User currentUser) {
        StudyGroup group = StudyGroup.builder()
                .groupName(request.getGroupName())
                .subject(request.getSubject())
                .maxMembers(request.getMaxMembers() != null ? request.getMaxMembers() : 10)
                .createdBy(currentUser)
                .build();

        StudyGroup savedGroup = studyGroupRepository.save(group);
        return mapToResponse(savedGroup);
    }

    private StudyGroupResponse mapToResponse(StudyGroup group) {
        return StudyGroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .subject(group.getSubject())
                .maxMembers(group.getMaxMembers())
                .memberCount(1) // Initial member count starting with creator
                .createdById(group.getCreatedBy().getUserId())
                .createdByName(group.getCreatedBy().getFullName())
                .createdAt(group.getCreatedAt())
                .build();
    }
}
