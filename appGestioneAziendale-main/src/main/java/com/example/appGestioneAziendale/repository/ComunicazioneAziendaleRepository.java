package com.example.appGestioneAziendale.repository;

import com.example.appGestioneAziendale.domain.entities.ComunicazioneAziendale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComunicazioneAziendaleRepository extends JpaRepository<ComunicazioneAziendale, Long> {

}
