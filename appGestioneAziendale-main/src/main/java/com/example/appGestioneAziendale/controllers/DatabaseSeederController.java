package com.example.appGestioneAziendale.controllers;

import com.example.appGestioneAziendale.services.DatabaseSeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indica che questa classe è un controller REST (restituisce risposte JSON o testo semplice).
public class DatabaseSeederController {

    // Inietta il servizio responsabile della logica di creazione/populazione del database.
    @Autowired
    private DatabaseSeederService databaseSeederService;

    /**
     * Endpoint per avviare il processo di popolazione del database.
     *
     * @return Una stringa di conferma ("Database popolato!") una volta completato il processo.
     */
    @PostMapping("/start-db") // Mappa questo metodo al verbo HTTP POST sull'endpoint "/start-db".
    public String createDb() {
        // Chiama il metodo del servizio responsabile della creazione e del popolamento del database.
        databaseSeederService.createDatabase();

        // Restituisce un messaggio di conferma all'utente.
        return "Database popolato!";
    }
}