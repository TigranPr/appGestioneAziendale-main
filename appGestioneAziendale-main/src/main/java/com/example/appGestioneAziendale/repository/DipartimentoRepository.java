package com.example.appGestioneAziendale.repository;

import com.example.appGestioneAziendale.domain.entities.Dipartimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DipartimentoRepository extends JpaRepository<Dipartimento, Long> {

}
