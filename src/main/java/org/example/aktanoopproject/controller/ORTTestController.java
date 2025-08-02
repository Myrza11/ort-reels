package org.example.aktanoopproject.controller;

import lombok.AllArgsConstructor;
import org.example.aktanoopproject.dto.ORTTestDTO;
import org.example.aktanoopproject.model.ORTTest;
import org.example.aktanoopproject.service.ORTTestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ort-test")
@AllArgsConstructor
public class ORTTestController {
    private final ORTTestService ortTestService;

    @GetMapping("/all")
    public List<ORTTest> getOrtTests() {
        return ortTestService.getOrtTests();
    }

    @PostMapping("/create")
    public ORTTest addOrtTest(@RequestBody ORTTestDTO ortTest) {
        return ortTestService.create(ortTest);
    }

    @GetMapping("get-byID")
    public ORTTest getOrtTestById(@RequestParam Long id) {
        return ortTestService.getOrtTestById(id);
    }

}
