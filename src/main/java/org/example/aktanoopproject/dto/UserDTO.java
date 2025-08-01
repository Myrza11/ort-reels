package org.example.aktanoopproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.aktanoopproject.model.Interest;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank
    private String name;
    private String surname;
    @Email
    @NotBlank
    private String email;
    @Size(min = 6)

    private String password;
    private Set<Interest> interest;
}
