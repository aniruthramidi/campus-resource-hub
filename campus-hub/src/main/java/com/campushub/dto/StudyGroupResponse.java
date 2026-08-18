package com.campushub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupResponse {
    private Long groupId;
    private String groupName;
    private String subject;
    private Long createdById;
    private String createdByName;
    private Integer maxMembers;
    private Integer memberCount;
    private LocalDateTime createdAt;
}
