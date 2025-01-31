package com.example.appGestioneAziendale.mappers;

import com.example.appGestioneAziendale.domain.dto.response.LikeResponse;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.domain.entities.Like;
import com.example.appGestioneAziendale.domain.entities.News;
import com.example.appGestioneAziendale.services.DipendenteService;
import com.example.appGestioneAziendale.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private NewsService newsService;

    public Like toEntity(News news, Dipendente dipendente) {
        return Like.builder()
                .news(news)
                .dipendente(dipendente)
                .build();
    }

    public LikeResponse toResponse(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getNews().getId(),
                like.getDipendente().getId()
        );
    }

}
