package com.example.appGestioneAziendale.services;

import com.example.appGestioneAziendale.domain.dto.requests.CreateCommentoRequest;
import com.example.appGestioneAziendale.domain.dto.requests.UpdateCommentoRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.entities.Commento;
import com.example.appGestioneAziendale.domain.exceptions.MyEntityNotFoundException;
import com.example.appGestioneAziendale.domain.exceptions.MyIllegalException;
import com.example.appGestioneAziendale.mappers.CommentoMapper;
import com.example.appGestioneAziendale.repository.CommentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentoService {
    @Autowired
    private CommentoRepository commentoRepository;
    @Autowired
    private CommentoMapper commentoMapper;
    @Autowired
    private NewsService newsService;
    @Autowired
    private DipendenteService dipendenteService;

    public Commento getById(Long id) {
        return commentoRepository.findById(id)
                .orElseThrow(() -> new MyEntityNotFoundException("Commento con id: " + id + " non trovato!"));
    }

    public List<Commento> getAll() {
        return commentoRepository.findAll();
    }

    public void deleteCommento(Long id) {
        Commento commento = getById(id);
        commentoRepository.deleteById(commento.getId());
    }

    public EntityIdResponse createCommento(CreateCommentoRequest request) {
        Commento commento = commentoRepository.save(commentoMapper.fromCreateCommentoRequest(request));
        return new EntityIdResponse(commento.getId());
    }

    public EntityIdResponse updateCommento(Long id, UpdateCommentoRequest request) {
        Commento commento = getById(id);
        if (request.testo() != null) commento.setTesto(request.testo());
        if (request.news() != null) commento.setNews(newsService.getById(request.news().id()));
        if (request.dipendente() != null) commento.setDipendente(dipendenteService.getById(request.dipendente().id()));
        if (request.testo() == null && request.news() == null && request.dipendente() == null)
            throw new MyIllegalException("Per fare un update devi almeno inserire un campo");
        return new EntityIdResponse(commentoRepository.save(commento).getId());
    }
}
