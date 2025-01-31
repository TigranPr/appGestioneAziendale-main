package com.example.appGestioneAziendale.domain.dto.response;

import lombok.Builder;

@Builder
public record DipartimentoResponse(
        Long id,
        String nome,
        String descrizione
) {
}
