package com.example.appGestioneAziendale.services;

import com.example.appGestioneAziendale.domain.dto.requests.ComunicazioneAziendaleRequest;
import com.example.appGestioneAziendale.domain.dto.requests.UpdateComunicazioneAziendaleRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.entities.ComunicazioneAziendale;
import com.example.appGestioneAziendale.domain.exceptions.MyEntityNotFoundException;
import com.example.appGestioneAziendale.domain.exceptions.MyIllegalException;
import com.example.appGestioneAziendale.mappers.ComunicazioneAziendaleMapper;
import com.example.appGestioneAziendale.repository.ComunicazioneAziendaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComunicazioneAziendaleService {
    @Autowired
    private ComunicazioneAziendaleRepository comunicazioneAziendaleRepository;
    @Autowired
    private ComunicazioneAziendaleMapper comunicazioneAziendaleMapper;
    @Autowired
    private DipendenteService dipendenteService;

    public ComunicazioneAziendale getById(Long id) throws MyEntityNotFoundException {
        return comunicazioneAziendaleRepository
                .findById(id)
                .orElseThrow(
                        () -> new MyEntityNotFoundException("Le comunicazioni aziendali con id " + id + " non esistono"));
    }

    public List<ComunicazioneAziendale> getAll() {
        return comunicazioneAziendaleRepository.findAll();
    }

    public EntityIdResponse createComunicazioneAziendale(Long idDipendente, ComunicazioneAziendaleRequest request) {
        ComunicazioneAziendale comunicazioneAziendale = comunicazioneAziendaleMapper.toEntity(idDipendente, request);
        return new EntityIdResponse(comunicazioneAziendaleRepository.save(comunicazioneAziendale).getId());
    }

    public EntityIdResponse updateComunicazioneAziendale(Long id, UpdateComunicazioneAziendaleRequest response) {
        ComunicazioneAziendale comunicazioneAziendale = getById(id);
        if (response.testo() != null) comunicazioneAziendale.setTesto(response.testo());
        if (response.allegato_url() != null) comunicazioneAziendale.setAllegato_url(response.allegato_url());
        if (response.idDipendente() != null)
            comunicazioneAziendale.setIdDipendente(dipendenteService.getById(response.idDipendente().id()));
        if (response.testo() == null && response.allegato_url() == null && response.idDipendente() == null)
            throw new MyIllegalException("Per fare un update devi almeno inserire un campo");
        return new EntityIdResponse(comunicazioneAziendaleRepository.save(comunicazioneAziendale).getId());
    }

    public void deleteById(Long id) {
        ComunicazioneAziendale comunicazioneAziendale = getById(id);
        comunicazioneAziendaleRepository.deleteById(comunicazioneAziendale.getId());
    }
}
