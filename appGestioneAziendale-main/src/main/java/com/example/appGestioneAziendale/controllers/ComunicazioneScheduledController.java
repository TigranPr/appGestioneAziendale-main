package com.example.appGestioneAziendale.controllers;

import com.example.appGestioneAziendale.domain.dto.requests.ComunicazioneScheduledRequest;
import com.example.appGestioneAziendale.domain.dto.requests.ComunicazioneScheduledUpdateRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.dto.response.GenericResponse;
import com.example.appGestioneAziendale.services.ComunicazioneScheduledService;
import jakarta.validation.Valid;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/scheduled_comunicazione")
public class ComunicazioneScheduledController {

    @Autowired
    private ComunicazioneScheduledService comunicazioneScheduledService;

    @PostMapping("/create")
    public ResponseEntity<EntityIdResponse> createComunicazioneScheduled(
            @Valid @RequestBody ComunicazioneScheduledRequest request)
            throws Exception {
        return new ResponseEntity<>(comunicazioneScheduledService.createComunicazioneScheduled(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityIdResponse> updateComunicazioneScheduled(@PathVariable Long id,
                                                                         @Valid @RequestBody ComunicazioneScheduledUpdateRequest request)
            throws SchedulerException {
        return new ResponseEntity<>(comunicazioneScheduledService.updateComunicazioneScheduled(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse> deleteById(@PathVariable Long id) throws Exception {
        comunicazioneScheduledService.deleteComunicazioneScheduledById(id);
        return new ResponseEntity<>(new GenericResponse(
                "Comunicazione schedulata con id " + id + " eliminata con successo"), HttpStatus.OK);
    }
}