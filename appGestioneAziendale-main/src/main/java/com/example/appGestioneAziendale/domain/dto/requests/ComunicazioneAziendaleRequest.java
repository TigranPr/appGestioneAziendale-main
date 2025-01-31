package com.example.appGestioneAziendale.domain.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record ComunicazioneAziendaleRequest(
        @NotBlank(message = "Il titolo non può essere blank o null")
        String titolo,
        @NotBlank(message = "Il testo non può essere blank o null")
        String testo,
        @NotBlank(message = "L'allegato non può essere blank o null")
        String allegato_url
) {
}
