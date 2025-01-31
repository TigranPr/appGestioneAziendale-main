package com.example.appGestioneAziendale.controllers;

import com.example.appGestioneAziendale.domain.dto.requests.CreateDipendenteRequest;
import com.example.appGestioneAziendale.domain.dto.requests.PosizioneLavorativaRequest;
import com.example.appGestioneAziendale.domain.dto.requests.UpdateDipendenteRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.dto.response.GenericResponse;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.domain.entities.PosizioneLavorativa;
import com.example.appGestioneAziendale.services.DipendenteService;
import com.example.appGestioneAziendale.services.PosizioneLavorativaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posizionelavorativa")
public class PosizioneLavorativaController {
    @Autowired
    private PosizioneLavorativaService posizioneLavorativaService;

    @GetMapping("/get/{id}")
    public ResponseEntity<PosizioneLavorativa> getById(@PathVariable Long id) {
        return new ResponseEntity<>(posizioneLavorativaService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PosizioneLavorativa>> getAll() {
        return new ResponseEntity<>(posizioneLavorativaService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<EntityIdResponse> create(@RequestBody @Valid PosizioneLavorativaRequest request) {
        return new ResponseEntity<>(posizioneLavorativaService.createPosizioneLavorativa(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityIdResponse> update(@PathVariable Long id, @RequestBody @Valid PosizioneLavorativaRequest request) {
        return new ResponseEntity<>(posizioneLavorativaService.updatePosizioneLavorativa(id, request), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse> deleteById(@PathVariable Long id) {
        posizioneLavorativaService.deleteById(id);
        return new ResponseEntity<>(
                new GenericResponse("Posizione lavorativa con id " + id + " eliminata correttamente"), HttpStatus.OK);
    }
}

