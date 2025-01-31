package com.example.appGestioneAziendale.domain.dto.requests;

import jakarta.validation.constraints.Future;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ComunicazioneScheduledUpdateRequest(
        @Future
        LocalDateTime publishTime
) {
}
