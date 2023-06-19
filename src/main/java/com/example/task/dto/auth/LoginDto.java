package com.example.task.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @Email(message = "Email required")
    private String email;

    @Size(min = 8, message = "Password required")
    private String password;


}
