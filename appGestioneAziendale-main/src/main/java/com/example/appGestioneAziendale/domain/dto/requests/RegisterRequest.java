package com.example.appGestioneAziendale.domain.dto.requests;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RegisterRequest(
        @NotBlank(message = "il nome non può essere vuoto")
        String nome,
        @NotBlank(message = "il cognome non può essere vuoto")
        String cognome,
        @Email(message = "mail non valida")
        String email,
        @NotBlank(message = "inserire una password")
        String password,
        @NotNull
        Long comune,
        @Past
        LocalDate dataDiNascita,
        @Pattern(
                regexp = "^\\+?[0-9]+$",
                message = "Telefono non valido")
        String telefono,
        String avatar
) {
}
