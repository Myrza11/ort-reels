package org.example.aktanoopproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MassageDTO {
    private String content;
    private Long taskId;
}
