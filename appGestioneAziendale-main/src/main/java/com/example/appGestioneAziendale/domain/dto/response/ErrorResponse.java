package com.example.appGestioneAziendale.domain.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String exception,
        String message
) {
}