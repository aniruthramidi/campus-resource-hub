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
public class ResourceResponse {
    private Long resourceId;
    private String title;
    private String description;
    private String subjectCode;
    private Integer semester;
    private String category;
    private String fileGcsUrl;
    private Long uploaderId;
    private String uploaderName;
    private Integer upvotes;
    private Boolean isUpvotedByMe;
    private Boolean isBookmarkedByMe;
    private LocalDateTime createdAt;
}
