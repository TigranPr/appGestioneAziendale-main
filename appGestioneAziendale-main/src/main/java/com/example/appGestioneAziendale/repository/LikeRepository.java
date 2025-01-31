package com.example.appGestioneAziendale.repository;

import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.domain.entities.Like;
import com.example.appGestioneAziendale.domain.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    //verifico se un dipendente ha già messo like a una news
    boolean existsByNewsAndDipendente(News news, Dipendente dipendente);

}
