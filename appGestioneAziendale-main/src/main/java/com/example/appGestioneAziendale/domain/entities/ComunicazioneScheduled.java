package com.example.appGestioneAziendale.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comunicazione_scheduled")
@EntityListeners(AuditingEntityListener.class)
public class ComunicazioneScheduled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titolo;
    @Column(nullable = false)
    private String testo;
    @Column(nullable = false)
    private LocalDateTime publishTime;
    @Column(nullable = false)
    private String allegato_url;
    @ManyToOne
    @JoinColumn(name = "id_publisher", referencedColumnName = "id")
    private Dipendente idDipendente;


}

