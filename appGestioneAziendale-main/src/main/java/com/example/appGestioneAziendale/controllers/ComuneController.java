package com.example.appGestioneAziendale.controllers;

import com.example.appGestioneAziendale.domain.dto.requests.ComuneRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.entities.Comune;
import com.example.appGestioneAziendale.services.ComuneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comuni")
public class ComuneController {
    @Autowired
    private ComuneService comuneService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Comune> getById(@PathVariable Long id) {
        return new ResponseEntity<>(comuneService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Comune>> getAll() {
        return new ResponseEntity<>(comuneService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<EntityIdResponse> create(@RequestBody @Valid ComuneRequest request) {
        return new ResponseEntity<>(comuneService.createComune(request), HttpStatus.CREATED);
    }
}
