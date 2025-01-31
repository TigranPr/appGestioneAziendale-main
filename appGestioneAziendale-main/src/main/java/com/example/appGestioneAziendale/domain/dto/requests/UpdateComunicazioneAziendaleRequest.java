package com.example.appGestioneAziendale.domain.dto.requests;

import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import lombok.Builder;

@Builder
public record UpdateComunicazioneAziendaleRequest(
        String testo,
        String allegato_url,
        EntityIdResponse idDipendente
) {
}
