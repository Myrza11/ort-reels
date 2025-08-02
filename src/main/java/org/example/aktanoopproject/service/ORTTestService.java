package org.example.aktanoopproject.service;

import lombok.RequiredArgsConstructor;
import org.example.aktanoopproject.dto.ORTTestDTO;
import org.example.aktanoopproject.model.ORTTest;
import org.example.aktanoopproject.repository.ORTTestRepository;
import org.example.aktanoopproject.repository.TestSetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ORTTestService {
    private final ORTTestRepository ortTestRepository;
    private final TestSetRepository testSetRepository;

    public ORTTest create(ORTTestDTO ortTestDTO) {
        ORTTest ortTest = new ORTTest();
        ortTest.setName(ortTestDTO.getName());
        ortTest.setTestSets(testSetRepository.findAllById(ortTestDTO.getTestSetsIds()));
        ortTestRepository.save(ortTest);
        return ortTest;
    }

    public List<ORTTest> getOrtTests() {
        return ortTestRepository.findAll();
    }
    public ORTTest getOrtTestById(Long id) {
        return ortTestRepository.findById(id).orElse(null);
    }
}
