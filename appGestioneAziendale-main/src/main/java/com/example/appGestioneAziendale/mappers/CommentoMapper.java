package com.example.appGestioneAziendale.mappers;

import com.example.appGestioneAziendale.domain.dto.requests.CreateCommentoRequest;
import com.example.appGestioneAziendale.domain.entities.Commento;
import com.example.appGestioneAziendale.services.DipendenteService;
import com.example.appGestioneAziendale.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentoMapper {
    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private NewsService newsService;

    public Commento fromCreateCommentoRequest(CreateCommentoRequest request) {
        return Commento.builder()
                .testo(request.testo())
                .news(newsService.getById(request.news().id()))
                .dipendente(dipendenteService.getById(request.dipendente().id()))
                .build();
    }
}
