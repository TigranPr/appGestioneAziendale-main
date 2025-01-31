package com.example.appGestioneAziendale.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comunicazione_aziendale")
@EntityListeners(AuditingEntityListener.class)
public class ComunicazioneAziendale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String testo;
    @Column(nullable = false)
    private String allegato_url;
    @ManyToOne
    @JoinColumn(name = "id_publisher", referencedColumnName = "id")
    private Dipendente idDipendente;

}
