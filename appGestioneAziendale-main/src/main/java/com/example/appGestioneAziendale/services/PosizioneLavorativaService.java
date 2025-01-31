package com.example.appGestioneAziendale.services;


import com.example.appGestioneAziendale.domain.dto.requests.PosizioneLavorativaRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.dto.response.PosizioneLavorativaResponse;
import com.example.appGestioneAziendale.domain.entities.PosizioneLavorativa;
import com.example.appGestioneAziendale.domain.exceptions.MyEntityNotFoundException;
import com.example.appGestioneAziendale.mappers.PosizioneLavorativaMapper;
import com.example.appGestioneAziendale.repository.PosizioneLavorativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosizioneLavorativaService {
    @Autowired
    private PosizioneLavorativaRepository posizioneLavorativaRepository;
    @Autowired
    private PosizioneLavorativaMapper posizioneLavorativaMapper;

    public PosizioneLavorativa getById(Long id) throws MyEntityNotFoundException {
        return posizioneLavorativaRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("la posizizone lavorativa con id " + id + " non esiste"));
    }

    public List<PosizioneLavorativa> getAll() {
        return posizioneLavorativaRepository.findAll();
    }

    public EntityIdResponse createPosizioneLavorativa(PosizioneLavorativaRequest request) {
        PosizioneLavorativa posizioneLavorativa = posizioneLavorativaMapper.fromPosizioneLavorativaRequest(request);
        return EntityIdResponse.
                builder()
                .id(posizioneLavorativaRepository.save(posizioneLavorativa).getId())
                .build();
    }

    public EntityIdResponse updatePosizioneLavorativa(Long id, PosizioneLavorativaRequest request) {
        PosizioneLavorativa posizioneLavorativa = getById(id);
        posizioneLavorativa.setDescrizione(request.descrizione());
        posizioneLavorativa.setNome(request.nome());
        return new EntityIdResponse(posizioneLavorativaRepository.save(posizioneLavorativa).getId());
    }

    public void deleteById(Long id) {
        posizioneLavorativaRepository.deleteById(id);
    }
}
