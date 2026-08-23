package com.campushub.service;

import com.campushub.dto.StudyGroupRequest;
import com.campushub.dto.StudyGroupResponse;
import com.campushub.entity.StudyGroup;
import com.campushub.entity.User;
import com.campushub.repository.StudyGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyGroupServiceTest {

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @InjectMocks
    private StudyGroupService studyGroupService;

    private User sampleUser;
    private StudyGroup sampleGroup;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .userId(1L)
                .fullName("Alex Rivera")
                .email("alex@campus.edu")
                .build();

        sampleGroup = StudyGroup.builder()
                .groupId(5L)
                .groupName("OS EndSem Squad")
                .subject("Operating Systems (CS401)")
                .maxMembers(10)
                .createdBy(sampleUser)
                .build();
    }

    @Test
    void testCreateGroupSuccess() {
        StudyGroupRequest request = new StudyGroupRequest("OS EndSem Squad", "Operating Systems (CS401)", 10);
        when(studyGroupRepository.save(any(StudyGroup.class))).thenReturn(sampleGroup);

        StudyGroupResponse response = studyGroupService.createGroup(request, sampleUser);

        assertNotNull(response);
        assertEquals("OS EndSem Squad", response.getGroupName());
        assertEquals("Alex Rivera", response.getCreatedByName());
    }

    @Test
    void testGetAllGroups() {
        when(studyGroupRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(sampleGroup));

        List<StudyGroupResponse> groups = studyGroupService.getAllGroups(null);

        assertNotNull(groups);
        assertEquals(1, groups.size());
        assertEquals("OS EndSem Squad", groups.get(0).getGroupName());
    }
}
