package com.example.appGestioneAziendale.services;

import com.example.appGestioneAziendale.domain.dto.requests.CreateDipendenteRequest;
import com.example.appGestioneAziendale.domain.dto.requests.UpdateDipendenteRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.domain.enums.Ruolo;
import com.example.appGestioneAziendale.domain.exceptions.MyEntityNotFoundException;
import com.example.appGestioneAziendale.domain.exceptions.MyIllegalException;
import com.example.appGestioneAziendale.mappers.DipendenteMapper;
import com.example.appGestioneAziendale.repository.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DipendenteService {
    @Autowired
    private DipendenteRepository dipendenteRepository;
    @Autowired
    private DipendenteMapper dipendenteMapper;
    @Autowired
    private ComuneService comuneService;

    public Dipendente getById(Long id) {
        return dipendenteRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Dipendente con id: " + id + " non trovato!"));
    }

    public List<Dipendente> getAll() {
        return dipendenteRepository.findAll();
    }

    public EntityIdResponse createDipendente(CreateDipendenteRequest request) {
        Dipendente dipendente = dipendenteRepository.save(dipendenteMapper.fromCreateDipendenteRequest(request));
        return new EntityIdResponse(dipendente.getId());
    }

    public void insertDipendente(Dipendente dipendente) {
        dipendenteRepository.save(dipendente);
    }

    public EntityIdResponse updateDipendente(Long id, UpdateDipendenteRequest request) {
        Dipendente myDipendente = getById(id);
        Ruolo ruolo;
        try {
            ruolo = Ruolo.valueOf(request.ruolo());
        } catch (IllegalArgumentException e) {
            throw new MyIllegalException("Ruolo inserito non valido!");
        }
        if (request.nome() != null) myDipendente.setNome(request.nome());
        if (request.cognome() != null) myDipendente.setCognome(request.cognome());
        if (request.email() != null) myDipendente.setEmail(request.email());
        if (request.comune_di_nascita() != null)
            myDipendente.setComuneDiNascita(comuneService.getById(request.comune_di_nascita().id()));
        if (request.data_di_nascita() != null) myDipendente.setDataDiNascita(request.data_di_nascita());
        if (request.telefono() != null) myDipendente.setTelefono(request.telefono());
        if (request.avatar() != null) myDipendente.setAvatar(request.avatar());
        if (request.ruolo() != null) myDipendente.setRuolo(ruolo);
        return new EntityIdResponse(dipendenteRepository.save(myDipendente).getId());
    }

    public Dipendente getByRegistrationToken(String token) {
        return dipendenteRepository
                .findByRegistrationToken(token)
                .orElseThrow(() -> new MyEntityNotFoundException("utente con token " + token + " non trovato"));
    }

    public Dipendente getByEmail(String email) throws MyEntityNotFoundException {
        return dipendenteRepository
                .findByEmail(email)
                .orElseThrow(() -> new MyEntityNotFoundException("utente con email " + email + " non trovato"));
    }

    public void deleteDipendente(Long id) {
        dipendenteRepository.deleteById(id);
    }
}
