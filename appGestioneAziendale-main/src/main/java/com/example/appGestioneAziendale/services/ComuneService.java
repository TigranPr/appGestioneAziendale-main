package com.example.appGestioneAziendale.services;

import com.example.appGestioneAziendale.domain.dto.requests.ComuneRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.entities.Comune;
import com.example.appGestioneAziendale.domain.exceptions.MyEntityNotFoundException;
import com.example.appGestioneAziendale.mappers.ComuneMapper;
import com.example.appGestioneAziendale.repository.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComuneService {
    @Autowired
    private ComuneRepository comuneRepository;
    @Autowired
    private ComuneMapper comuneMapper;

    public EntityIdResponse createComune(ComuneRequest newComune) {
        Comune comune = comuneRepository.save(comuneMapper.fromCreateComuneRequest(newComune));
        return new EntityIdResponse(comune.getId());
    }

    public Comune getById(Long id) throws MyEntityNotFoundException {
        return comuneRepository.findById(id)
                .orElseThrow(() -> new MyEntityNotFoundException("Comune non trovato"));
    }

    public List<Comune> getAll() {
        return comuneRepository.findAll();
    }
}
