package org.example.aktanoopproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateTestRequestDTO {
    private String title;
    private int count;
    private int timeLimitMinutes;
    private List<Long> questionIds;
}
