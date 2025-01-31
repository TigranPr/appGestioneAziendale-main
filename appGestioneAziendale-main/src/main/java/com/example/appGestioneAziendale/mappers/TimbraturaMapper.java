package com.example.appGestioneAziendale.mappers;

import com.example.appGestioneAziendale.domain.dto.requests.TimbraturaRequest;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.domain.entities.Timbratura;
import com.example.appGestioneAziendale.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimbraturaMapper {
    @Autowired
    private DipendenteService dipendenteService;

    public Timbratura fromTimbraturaRequest(TimbraturaRequest request) {
        Dipendente dipendente = dipendenteService.getById(request.idDipendente());
        if (request.ingresso() == null && request.inizioPausa() == null && request.finePausa() == null && request.uscita() == null)
            System.out.println("Ma che richiesta mi stai facendo?");

        return Timbratura.builder()
                .ingresso(LocalDateTime.now())
                .uscita(request.uscita())
                .inizioPausaPranzo(request.inizioPausa())
                .finePausaPranzo(request.finePausa())
                .idDipendente(dipendente)
                .build();
    }
}
