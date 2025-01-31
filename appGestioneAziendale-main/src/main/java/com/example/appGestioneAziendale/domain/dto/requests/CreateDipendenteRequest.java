package com.example.appGestioneAziendale.domain.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreateDipendenteRequest(
        @NotBlank(message = "Il nome non può essere blank o null")
        String nome,
        @NotBlank(message = "Il cognome non può essere blank o null")
        String cognome,
        @Email(message = "Email non valida")
        String email,
        @NotBlank(message = "La password non può essere blank o null")
        String password,
        @NotNull(message = "Comune non puo essere null")
        EntityIdRequest comune_di_nascita,
        @Past
        LocalDate data_di_nascita,
        @NotBlank(message = "Il numero di telefono non può essere blank o null")
        String telefono,
        @NotBlank(message = "Problemi con l'Avatar")
        String avatar,
        @NotBlank(message = "Il ruolo non può essere blank o null")
        String ruolo,
        @NotNull
        Long idPosizioneLavorativa
) {
}
