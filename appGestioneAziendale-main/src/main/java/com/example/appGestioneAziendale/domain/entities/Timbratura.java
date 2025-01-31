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
@Table(name = "timbratura")
@EntityListeners(AuditingEntityListener.class)
public class Timbratura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime ingresso;
    @Column
    private LocalDateTime uscita;
    @Column(name = "inizio_pausa_pranzo")
    private LocalDateTime inizioPausaPranzo;
    @Column(name = "fine_pausa_pranzo")
    private LocalDateTime finePausaPranzo;
    @ManyToOne
    @JoinColumn(name = "id_dipendente", referencedColumnName = "id")
    private Dipendente idDipendente;
}
