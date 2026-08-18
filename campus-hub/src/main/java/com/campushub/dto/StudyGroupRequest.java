package com.campushub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupRequest {

    @NotBlank(message = "Group name is required")
    private String groupName;

    @NotBlank(message = "Subject is required")
    private String subject;

    @Min(value = 2, message = "Maximum members must be at least 2")
    @Max(value = 100, message = "Maximum members limit cannot exceed 100")
    private Integer maxMembers;
}
