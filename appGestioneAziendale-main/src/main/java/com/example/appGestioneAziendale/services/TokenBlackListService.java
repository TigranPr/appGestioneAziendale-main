package com.example.appGestioneAziendale.services;

import com.example.appGestioneAziendale.domain.entities.TokenBlackList;
import com.example.appGestioneAziendale.repository.TokenBlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {
    @Autowired
    private TokenBlackListRepository tokenBlackListRepository;
    @Autowired
    private DipendenteService dipendenteService;

    public Boolean isPresentToken(String token) {
        return tokenBlackListRepository.getByToken(token).isPresent();
    }

    public void insertToken(Long id_utente, String token) {
        TokenBlackList tokenBlackList = TokenBlackList
                .builder()
                .token(token)
                .dipendente(dipendenteService.getById(id_utente))
                .build();
        tokenBlackListRepository.save(tokenBlackList);
    }
}
