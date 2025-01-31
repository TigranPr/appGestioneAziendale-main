package com.example.appGestioneAziendale.controllers;

import com.example.appGestioneAziendale.services.DatabaseSeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseSeederController {
    @Autowired
    private DatabaseSeederService databaseSeederService;

    @PostMapping("/start-db")
    public String createDb() {
        databaseSeederService.createDatabase();
        return "Database popolato!";
    }
}
