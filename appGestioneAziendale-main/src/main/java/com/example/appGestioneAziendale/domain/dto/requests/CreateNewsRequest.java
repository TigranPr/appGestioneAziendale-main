package com.example.appGestioneAziendale.domain.dto.requests;

import com.example.appGestioneAziendale.domain.entities.Dipendente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateNewsRequest (
        @NotBlank(message = "il titolo non può essere vuoto")
        String titolo,
        @NotBlank(message = "il testo non può essere vuoto")
        String testo,
        @NotNull
        String image_url,
        @NotNull
        String allegato,
        @NotNull
        Long idPublisher
){
}
