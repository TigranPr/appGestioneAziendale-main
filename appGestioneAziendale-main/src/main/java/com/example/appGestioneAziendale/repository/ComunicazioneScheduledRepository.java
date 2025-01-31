package com.example.appGestioneAziendale.repository;

import com.example.appGestioneAziendale.domain.entities.ComunicazioneScheduled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComunicazioneScheduledRepository extends JpaRepository<ComunicazioneScheduled, Long> {
}
