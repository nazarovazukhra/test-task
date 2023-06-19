package com.example.task.dto.user;

import com.example.task.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDto {

    private Long id;
    private String firstname;
    private String lastname;

    private String email;

    private UserRole role;

    private LocalDateTime createdDate;


}
