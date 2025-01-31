package com.example.appGestioneAziendale.repository;

import com.example.appGestioneAziendale.domain.entities.Commento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Long> {
}
