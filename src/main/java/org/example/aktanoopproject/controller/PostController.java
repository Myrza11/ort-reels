package org.example.aktanoopproject.controller;

import org.example.aktanoopproject.dto.PostDTO;
import org.example.aktanoopproject.model.Post;
import org.example.aktanoopproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;


}
