package com.example.appGestioneAziendale.services;

import com.example.appGestioneAziendale.domain.dto.requests.*;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class DatabaseSeederService {
    @Autowired
    private ComuneService comuneService;
    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private DipartimentoService dipartimentoService;
    @Autowired
    private PosizioneLavorativaService posizioneLavorativaService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentoService commentoService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private ComunicazioneAziendaleService comunicazioneAziendaleService;

    public void createDatabase() {
        Faker faker = new Faker(Locale.ITALIAN);
        List<Long> comuniId = new ArrayList<>();
        List<Long> dipartimentiId = new ArrayList<>();
        List<Long> posizioniLavorativeId = new ArrayList<>();
        List<Long> dipendentiId = new ArrayList<>();
        List<Long> newsId = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            comuniId.add(comuneService.createComune(new ComuneRequest(faker.gameOfThrones().city(), faker.elderScrolls().city())).id());
        }
        for (int i = 0; i < 3; i++) {
            dipartimentiId.add(dipartimentoService.createDipartimento(new DipartimentoRequest(faker.pokemon().location(), faker.leagueOfLegends().champion())).id());
        }
        for (int i = 0; i < 3; i++) {
            posizioniLavorativeId.add(posizioneLavorativaService.createPosizioneLavorativa(new PosizioneLavorativaRequest(faker.company().profession(), faker.chuckNorris().fact(), dipartimentiId.get(new Random().nextInt(dipartimentiId.size())))).id());
        }
        for (int i = 0; i < 5; i++) {
            dipendentiId.add(dipendenteService.createDipendente(new CreateDipendenteRequest(
                    faker.dragonBall().character(),
                    faker.funnyName().name(),
                    faker.internet().emailAddress(),
                    "1234",
                    new EntityIdRequest(comuniId.get(new Random().nextInt(comuniId.size()))),
                    LocalDate.of(1990, 02, 25),
                    faker.phoneNumber().cellPhone(),
                    faker.avatar().image(),
                    "UTENTE",
                    posizioniLavorativeId.get(new Random().nextInt(posizioniLavorativeId.size()))
            )).id());
        }
        for (int i = 0; i < 3; i++) {
            newsId.add(newsService.createNews(new CreateNewsRequest(faker.book().title(), faker.chuckNorris().fact(), faker.internet().image(), faker.internet().image(), dipendentiId.get(new Random().nextInt(dipendentiId.size())))).id());
        }
        for (int i = 0; i < 3; i++) {
            commentoService.createCommento(new CreateCommentoRequest(faker.chuckNorris().fact(), new EntityIdRequest(newsId.get(new Random().nextInt(newsId.size()))), new EntityIdRequest(dipendentiId.get(new Random().nextInt(dipendentiId.size())))));
        }
        for (Long aLong : newsId) {
            likeService.createLike(new LikeRequest(aLong, dipendentiId.get(new Random().nextInt(dipendentiId.size()))));
        }
        for (int i = 0; i < 3; i++) {
            comunicazioneAziendaleService.createComunicazioneAziendale(dipendentiId.get(new Random().nextInt(dipendentiId.size())), new ComunicazioneAziendaleRequest(faker.pokemon().name(), faker.book().title(), faker.file().fileName()));
        }
    }
}
