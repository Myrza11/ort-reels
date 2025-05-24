package org.example.aktanoopproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskCreateDto {
    private String question; // base64
    private List<String> answers;
    private int correctIndex; // индекс правильного ответа (0..3)
}
