package com.example.appGestioneAziendale.mappers;

import com.example.appGestioneAziendale.domain.dto.requests.CreateNewsRequest;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.domain.entities.News;
import com.example.appGestioneAziendale.domain.exceptions.MyEntityNotFoundException;
import com.example.appGestioneAziendale.repository.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsMapper {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    public News fromCreateNewsRequest(CreateNewsRequest request) throws MyEntityNotFoundException {
            Dipendente dipendente = dipendenteRepository
            .findById(request.idPublisher())
            .orElseThrow(() -> new IllegalArgumentException("Dipartimento con ID : " + request.idPublisher() + " non trovato"));

        return News.builder()
                .titolo(request.titolo())
                .testo(request.testo())
                .image_url(request.image_url())
                .allegato_url(request.allegato())
                .idPublisher(dipendente)
                .build();
    }
}
