package org.example.aktanoopproject.dto;

import jakarta.persistence.OneToMany;
import lombok.Data;
import org.example.aktanoopproject.model.TestSet;

import java.util.List;
@Data
public class ORTTestDTO {
    private String name;
    private List<Long> testSetsIds;
}
