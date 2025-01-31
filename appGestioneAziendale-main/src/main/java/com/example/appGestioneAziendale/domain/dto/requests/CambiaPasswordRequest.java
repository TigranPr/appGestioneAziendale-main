package com.example.appGestioneAziendale.domain.dto.requests;

public record CambiaPasswordRequest(
        String vecchiaPassword,
        String nuovaPassword
) {
}
