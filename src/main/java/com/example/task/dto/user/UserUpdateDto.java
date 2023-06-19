package com.example.task.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
