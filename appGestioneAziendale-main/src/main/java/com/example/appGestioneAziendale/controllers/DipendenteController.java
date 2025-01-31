package com.example.appGestioneAziendale.controllers;

import com.example.appGestioneAziendale.domain.dto.requests.CreateDipendenteRequest;
import com.example.appGestioneAziendale.domain.dto.requests.UpdateDipendenteRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.dto.response.GenericResponse;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.services.DipendenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {
    @Autowired
    private DipendenteService dipendenteService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Dipendente> getById(@PathVariable Long id) {
        return new ResponseEntity<>(dipendenteService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Dipendente>> getAll() {
        return new ResponseEntity<>(dipendenteService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<EntityIdResponse> create(@RequestBody @Valid CreateDipendenteRequest request) {
        return new ResponseEntity<>(dipendenteService.createDipendente(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityIdResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateDipendenteRequest request) {
        return new ResponseEntity<>(dipendenteService.updateDipendente(id, request), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse> deleteById(@PathVariable Long id) {
        dipendenteService.deleteDipendente(id);
        return new ResponseEntity<>(
                new GenericResponse("Dipendente con id " + id + " eliminato correttamente"), HttpStatus.OK);
    }
}
