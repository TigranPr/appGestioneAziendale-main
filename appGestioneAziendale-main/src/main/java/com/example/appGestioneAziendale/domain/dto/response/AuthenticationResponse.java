package com.example.appGestioneAziendale.domain.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
}
