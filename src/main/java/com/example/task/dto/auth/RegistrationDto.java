package com.example.task.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {

    @NotBlank(message = "Firstname required")
    private String firstname;

    @NotBlank(message = "Lastname required")
    private String lastname;

    @Email(message = "Email required")
    private String email;

    @NotBlank
    private String password;


}
