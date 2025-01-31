package com.example.appGestioneAziendale.domain.dto.requests;

import com.example.appGestioneAziendale.domain.entities.Dipendente;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record TimbraturaRequest(
        LocalDateTime ingresso,
        LocalDateTime uscita,
        LocalDateTime inizioPausa,
        LocalDateTime finePausa,
        @NotEmpty
        Long idDipendente
) {
}
