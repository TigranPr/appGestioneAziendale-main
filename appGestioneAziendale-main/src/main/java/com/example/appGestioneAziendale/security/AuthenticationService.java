package com.example.appGestioneAziendale.security;

import com.example.appGestioneAziendale.domain.dto.requests.AuthRequest;
import com.example.appGestioneAziendale.domain.dto.requests.CambiaPasswordRequest;
import com.example.appGestioneAziendale.domain.dto.requests.RegisterRequest;
import com.example.appGestioneAziendale.domain.dto.response.AuthenticationResponse;
import com.example.appGestioneAziendale.domain.dto.response.ErrorResponse;
import com.example.appGestioneAziendale.domain.dto.response.GenericResponse;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.domain.enums.Ruolo;
import com.example.appGestioneAziendale.services.ComuneService;
import com.example.appGestioneAziendale.services.DipendenteService;
import com.example.appGestioneAziendale.services.TokenBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {
    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ComuneService comuneService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenBlackListService tokenBlackListService;
    @Autowired
    private JavaMailSender javaMailSender;


    public String register(RegisterRequest request) {
        Dipendente dipendente = Dipendente
                .builder()
                .nome(request.nome())
                .cognome(request.cognome())
                .email(request.email())
                .avatar(request.avatar())
                .dataDiNascita(request.dataDiNascita())
                .password(passwordEncoder.encode(request.password()))
                .telefono(request.telefono())
                .comuneDiNascita(comuneService.getById(request.comune()))
                .ruolo(Ruolo.DA_CONFERMARE)
                .build();
        String jwtToken = jwtService.generateToken(dipendente);
        dipendente.setRegistrationToken(jwtToken);
        dipendenteService.insertDipendente(dipendente);
        String confirmationUrl = "http://localhost:8080/auth/conferma?token=" + dipendente.getRegistrationToken();
        javaMailSender.send(createConfirmationEmail(dipendente.getEmail(), confirmationUrl));
        AuthenticationResponse.builder().token(jwtToken).build();
        ;
        return "Dipendente in fase di registrazione, prego confermare la emil!";
    }

    private SimpleMailMessage createConfirmationEmail(String email, String confirmationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email); // a chi mando la mail
        message.setReplyTo("fcramerotti91@gmail.com"); // a chi rispondo se faccio "rispondi"
        message.setFrom("fcramerotti91@gmail.com"); // da chi viene la mail
        message.setSubject("CONFERMA REGISTRAZIONE, SEMPRE FORZA MAGGGICA");
        message.setText("Ciao lupacchiotto, clicca qui per essere un vero tifoso DA MAGGGICAAA " + confirmationUrl);
        return message;
    }

    public GenericResponse confirmRegistration(String token) {
        Dipendente dipendente = dipendenteService.getByRegistrationToken(token);
        dipendente.setRuolo(Ruolo.UTENTE);
        dipendenteService.insertDipendente(dipendente);
        return GenericResponse
                .builder()
                .message("Account verificato con successo!")
                .build();
    }

    public Object changePassword(Long idDipendente, CambiaPasswordRequest request) {
        Dipendente dipendente = dipendenteService.getById(idDipendente);
        if (!passwordEncoder.matches(request.vecchiaPassword(), dipendente.getPassword())) {
            return ErrorResponse
                    .builder()
                    .exception("WrongPasswordException")
                    .message("La vecchia password non è corretta")
                    .build();
        }
        dipendente.setPassword(passwordEncoder.encode(request.nuovaPassword()));
        dipendenteService.insertDipendente(dipendente);
        return GenericResponse
                .builder()
                .message("Password cambiata con successo")
                .build();
    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.mail().toLowerCase(),
                request.password()
        ));
        Dipendente dipendente = dipendenteService.getByEmail(request.mail());
        String token = jwtService.generateToken(dipendente);
        dipendente.setLastLogin(LocalDateTime.now());
        dipendenteService.insertDipendente(dipendente);
        return AuthenticationResponse.builder().token(token).build();
    }

    public GenericResponse logout(Long idUtente, String token) {
        tokenBlackListService.insertToken(idUtente, token);
        return GenericResponse.builder().message("Logout effettuato con successo").build();
    }
}
