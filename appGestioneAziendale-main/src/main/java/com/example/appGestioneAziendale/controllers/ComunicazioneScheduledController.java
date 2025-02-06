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

/**
 * Controller REST per gestire le comunicazioni schedulate. Fornisce API per creare, aggiornare ed eliminare comunicazioni.
 */
@RestController // Indica che questa classe è un controller che gestisce risposte JSON.
@RequestMapping("/app/scheduled_comunicazione") // Prefisso URL comune per tutti gli endpoint di questo controller.
public class ComunicazioneScheduledController {

    @Autowired // Iniezione automatica del servizio responsabile della logica aziendale.
    private ComunicazioneScheduledService comunicazioneScheduledService;

    /**
     * Gestisce la creazione di una nuova comunicazione schedulata.
     *
     * @param request Oggetto DTO con i dettagli della comunicazione da creare.
     *                 Es. nome, dettagli, orari, destinatari, ecc.
     * @return Restituisce una risposta contenente l'ID della comunicazione appena creata.
     * @throws Exception Se si verifica un errore durante la creazione.
     */
    @PostMapping("/create") // Mappa questo metodo al verbo HTTP POST sull'endpoint "/create".
    public ResponseEntity<EntityIdResponse> createComunicazioneScheduled(
            // Usa @Valid per convalidare il corpo della richiesta (basato sulle annotazioni nel DTO).
            @Valid @RequestBody ComunicazioneScheduledRequest request)
            throws Exception { // Solleva un'eccezione in caso di errore durante la creazione.
        // Chiama il servizio per creare una nuova comunicazione e restituisce la risposta con stato HTTP 201 (CREATED).
        return new ResponseEntity<>(comunicazioneScheduledService.createComunicazioneScheduled(request), HttpStatus.CREATED);
    }

    /**
     * Gestisce l'aggiornamento di una comunicazione schedulata esistente.
     *
     * @param id L'ID della comunicazione da aggiornare.
     * @param request Oggetto DTO con i dettagli aggiornati per la comunicazione.
     * @return Restituisce una risposta indicando l'ID della comunicazione aggiornata.
     * @throws SchedulerException Se si verifica un errore relativo allo scheduler durante l'aggiornamento.
     */
    @PutMapping("/update/{id}") // Mappa questo metodo al verbo HTTP PUT sull'endpoint "/update/{id}".
    public ResponseEntity<EntityIdResponse> updateComunicazioneScheduled(
            // Legge il parametro "id" dalla path dell'URL.
            @PathVariable Long id,
            // Valida il corpo della richiesta aggiornata (DTO).
            @Valid @RequestBody ComunicazioneScheduledUpdateRequest request)
            throws SchedulerException { // Solleva un'eccezione se si verifica un problema con lo scheduling.
        // Chiama il servizio per aggiornare la comunicazione specificata e restituisce la risposta con stato HTTP 200 (OK).
        return new ResponseEntity<>(comunicazioneScheduledService.updateComunicazioneScheduled(id, request), HttpStatus.OK);
    }

    /**
     * Gestisce l'eliminazione di una comunicazione schedulata specificando il suo ID.
     *
     * @param id L'ID della comunicazione da eliminare.
     * @return Una risposta generica che notifica all'utente l'esito dell'operazione.
     * @throws Exception Se qualcosa va storto durante l'eliminazione.
     */
    @DeleteMapping("/delete/{id}") // Mappa questo metodo al verbo HTTP DELETE sull'endpoint "/delete/{id}".
    public ResponseEntity<GenericResponse> deleteById(
            // Legge il parametro "id" dalla path dell'URL.
            @PathVariable Long id)
            throws Exception { // Solleva un'eccezione se si verifica un errore durante l'eliminazione.
        // Chiama il servizio per eliminare la comunicazione specificata.
        comunicazioneScheduledService.deleteComunicazioneScheduledById(id);

        // Costruisce una risposta con un messaggio di conferma e stato HTTP 200 (OK).
        return new ResponseEntity<>(new GenericResponse(
                "Comunicazione schedulata con id " + id + " eliminata con successo"), HttpStatus.OK);
    }
}