package org.example.aktanoopproject.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.aktanoopproject.model.Interest;

import java.util.Set;

@Data
public class UserUpdateDTO {
    private String name;

    private String surname;

    @Size(min = 6)
    private String password;

    private Interest interests;

}
