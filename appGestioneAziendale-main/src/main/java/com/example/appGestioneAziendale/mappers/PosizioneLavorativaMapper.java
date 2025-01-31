package com.example.appGestioneAziendale.mappers;


import com.example.appGestioneAziendale.domain.dto.requests.PosizioneLavorativaRequest;
import com.example.appGestioneAziendale.domain.entities.Dipartimento;
import com.example.appGestioneAziendale.domain.entities.PosizioneLavorativa;
import com.example.appGestioneAziendale.repository.DipartimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PosizioneLavorativaMapper {

    @Autowired
    private DipartimentoRepository dipartimentoRepository;

    public PosizioneLavorativa fromPosizioneLavorativaRequest(PosizioneLavorativaRequest request) {

        Dipartimento dipartimento = dipartimentoRepository
                .findById(request.idDipartimento())
                .orElseThrow(() -> new IllegalArgumentException("Dipartimento con ID : " + request.idDipartimento() + " non trovato"));


        return PosizioneLavorativa
                .builder()
                .nome(request.nome())
                .descrizione(request.descrizione())
                .idDipartimento(dipartimento)
                .build();
    }
}




