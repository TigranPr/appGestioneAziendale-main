package com.example.appGestioneAziendale.mappers;

import com.example.appGestioneAziendale.domain.dto.requests.ComuneRequest;
import com.example.appGestioneAziendale.domain.entities.Comune;
import org.springframework.stereotype.Service;

@Service
public class ComuneMapper {

    public Comune fromCreateComuneRequest(ComuneRequest request) {
        return Comune.builder()
                .nome(request.nome())
                .regione(request.regione())
                .build();
    }
}
