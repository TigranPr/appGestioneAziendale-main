package com.example.appGestioneAziendale.domain.dto.response;

import lombok.Builder;

@Builder

public record PosizioneLavorativaResponse(

        Long id,

        String nome,

        String descrizione,

        Long idDipartimento
) {}
