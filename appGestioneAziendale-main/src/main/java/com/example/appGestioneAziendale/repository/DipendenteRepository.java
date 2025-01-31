package com.example.appGestioneAziendale.repository;

import com.example.appGestioneAziendale.domain.entities.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {

    Optional<Dipendente> findByEmail(String email);
    Optional<Dipendente> findByRegistrationToken(String token);
}
