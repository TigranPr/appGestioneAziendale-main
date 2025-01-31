package com.example.appGestioneAziendale.domain.entities;

import com.example.appGestioneAziendale.domain.enums.Ruolo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dipendente")
@EntityListeners(AuditingEntityListener.class)
public class Dipendente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToOne(optional = false)
    @JoinColumn(name = "comune_di_nascita", referencedColumnName = "nome")
    private Comune comuneDiNascita;
    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataDiNascita;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    private String avatar;
    @Column(nullable = false)
    private Ruolo ruolo;
    @Column(name = "registration_token")
    private String registrationToken;
    @ManyToOne
    @JoinColumn(name = "id_posizione_lavorativa", referencedColumnName = "id")
    private PosizioneLavorativa posizioneLavorativa;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;
    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
