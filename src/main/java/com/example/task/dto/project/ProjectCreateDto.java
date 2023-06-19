package com.example.task.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectCreateDto {

    @Size(min = 3, max = 50, message = "Name required")
    private String name;

    @Size(min = 20, max = 200, message = "Description required")
    private String description;
}
