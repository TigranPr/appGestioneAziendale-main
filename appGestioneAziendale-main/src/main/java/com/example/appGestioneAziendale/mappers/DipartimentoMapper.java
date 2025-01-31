package com.example.appGestioneAziendale.mappers;

import com.example.appGestioneAziendale.domain.dto.requests.DipartimentoRequest;
import com.example.appGestioneAziendale.domain.dto.response.DipartimentoResponse;
import com.example.appGestioneAziendale.domain.entities.Dipartimento;
import org.springframework.stereotype.Component;

@Component
public class DipartimentoMapper {
    public Dipartimento toEntity(DipartimentoRequest request) {
        return Dipartimento.builder()
                .nome(request.nome())
                .descrizione(request.descrizione())
                .build();
    }

    public DipartimentoResponse toResponse(Dipartimento dipartimento) {
        return new DipartimentoResponse(
                dipartimento.getId(),
                dipartimento.getNome(),
                dipartimento.getDescrizione()
        );
    }
}
