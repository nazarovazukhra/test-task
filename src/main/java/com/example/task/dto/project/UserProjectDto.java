package com.example.task.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProjectDto {

    @Min(value = 0, message = "project id must be greater than 0")
    private Long projectId;

    @NotBlank(message = "User id is required")
    private Long userId;

    private String roleName;

}
