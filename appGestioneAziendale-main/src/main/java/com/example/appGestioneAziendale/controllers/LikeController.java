package com.example.appGestioneAziendale.controllers;

import com.example.appGestioneAziendale.domain.dto.requests.LikeRequest;
import com.example.appGestioneAziendale.domain.dto.response.LikeResponse;
import com.example.appGestioneAziendale.domain.entities.Like;
import com.example.appGestioneAziendale.services.LikeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/create")
    public ResponseEntity<LikeResponse> createLike(@Valid @RequestBody LikeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.createLike(request));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Like> getLikeById(@PathVariable Long id) {
        return new ResponseEntity<>(likeService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Like>> getAllLikes() {
        return new ResponseEntity<>(likeService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLikeById(@PathVariable Long id) {
        likeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
