package com.example.appGestioneAziendale.controllers;

import com.example.appGestioneAziendale.domain.dto.requests.DipartimentoRequest;
import com.example.appGestioneAziendale.domain.dto.response.DipartimentoResponse;
import com.example.appGestioneAziendale.domain.entities.Dipartimento;
import com.example.appGestioneAziendale.services.DipartimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dipartimenti")
public class DipartimentoController {
    @Autowired
    private DipartimentoService dipartimentoService;

    @PostMapping("/create")
    public ResponseEntity<DipartimentoResponse> createDipartimento(@Valid @RequestBody DipartimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dipartimentoService.createDipartimento(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Dipartimento>> getAll() {
        return ResponseEntity.ok(dipartimentoService.getAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DipartimentoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dipartimentoService.getById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DipartimentoResponse> updateDipartimento(
            @PathVariable Long id,
            @Valid @RequestBody DipartimentoRequest request
    ) {
        return ResponseEntity.ok(dipartimentoService.updateDipartimento(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        dipartimentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
