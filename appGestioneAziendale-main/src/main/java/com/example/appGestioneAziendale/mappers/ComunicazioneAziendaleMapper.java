package com.example.appGestioneAziendale.mappers;

import com.example.appGestioneAziendale.domain.dto.requests.ComunicazioneAziendaleRequest;
import com.example.appGestioneAziendale.domain.entities.ComunicazioneAziendale;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComunicazioneAziendaleMapper {

    @Autowired
    private DipendenteService dipendenteService;

    public ComunicazioneAziendale toEntity(Long id, ComunicazioneAziendaleRequest request) {
        Dipendente dipendente = dipendenteService.getById(id);
        return ComunicazioneAziendale.builder()
                .testo(request.testo())
                .allegato_url(request.allegato_url())
                .idDipendente(dipendente)
                .build();
    }

}
