package com.example.appGestioneAziendale.domain.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record DipartimentoRequest(
        @NotBlank(message = "Il nome non può essere vuoto")
        String nome,
        @NotBlank(message = "La descrizione non può essere vuota")
        String descrizione

) {
}
