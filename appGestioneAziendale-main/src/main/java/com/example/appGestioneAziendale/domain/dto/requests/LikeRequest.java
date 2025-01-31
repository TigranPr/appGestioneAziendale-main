package com.example.appGestioneAziendale.domain.dto.requests;
import jakarta.validation.constraints.NotNull;

public record LikeRequest(
    @NotNull(message = "NewsId non può essere blank o null")
    Long NewsId,
    @NotNull(message = "DipendenteId non può essere blank o null")
    Long DipendenteId
) {
}
