package com.example.appGestioneAziendale.domain.dto.requests;

public record UpdateCommentoRequest(
        String testo,
        EntityIdRequest news,
        EntityIdRequest dipendente
) {
}
