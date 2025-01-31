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
@Table(name = "posizione_lavorativa")
@EntityListeners(AuditingEntityListener.class)
public class PosizioneLavorativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "id_dipartimento", referencedColumnName = "id")
    private Dipartimento idDipartimento;
}
