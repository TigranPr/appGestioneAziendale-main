package com.example.appGestioneAziendale.domain.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record NewsResponse(
        Long id,
        String titolo,
        String testo,
        String image_url,
        String allegato,
        Long idPublisher
) {
}
