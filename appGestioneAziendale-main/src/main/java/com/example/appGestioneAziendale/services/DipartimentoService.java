package com.example.appGestioneAziendale.services;

import com.example.appGestioneAziendale.domain.dto.requests.DipartimentoRequest;
import com.example.appGestioneAziendale.domain.dto.response.DipartimentoResponse;
import com.example.appGestioneAziendale.domain.entities.Dipartimento;
import com.example.appGestioneAziendale.domain.exceptions.MyEntityNotFoundException;
import com.example.appGestioneAziendale.mappers.DipartimentoMapper;
import com.example.appGestioneAziendale.repository.DipartimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DipartimentoService {
    @Autowired
    private DipartimentoRepository dipartimentoRepository;

    @Autowired
    private DipartimentoMapper dipartimentoMapper;

    public DipartimentoResponse createDipartimento(DipartimentoRequest request) {
        Dipartimento dipartimento = dipartimentoMapper.toEntity(request);
        return dipartimentoMapper.toResponse(dipartimentoRepository.save(dipartimento));
    }

    public List<Dipartimento> getAll() {
        return dipartimentoRepository.findAll();
    }

    public DipartimentoResponse getById(Long id) {
        Dipartimento dipartimento = dipartimentoRepository.findById(id)
                .orElseThrow(() -> new MyEntityNotFoundException("Dipartimento con ID " + id + " non trovato"));
        return dipartimentoMapper.toResponse(dipartimento);
    }

    public DipartimentoResponse updateDipartimento(Long id, DipartimentoRequest request) {
        Dipartimento dipartimento = dipartimentoRepository.findById(id)
                .orElseThrow(() -> new MyEntityNotFoundException("Dipartimento con ID " + id + " non trovato"));

        dipartimento.setNome(request.nome());
        dipartimento.setDescrizione(request.descrizione());

        return dipartimentoMapper.toResponse(dipartimentoRepository.save(dipartimento));
    }

    public void deleteById(Long id) {
        if (!dipartimentoRepository.existsById(id)) {
            throw new MyEntityNotFoundException("Dipartimento con ID " + id + " non trovato");
        }
        dipartimentoRepository.deleteById(id);
    }

}
