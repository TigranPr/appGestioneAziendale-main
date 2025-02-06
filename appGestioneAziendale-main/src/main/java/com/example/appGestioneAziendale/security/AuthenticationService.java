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

// Indica che questa classe definisce una logica di business e sarà gestita da Spring
@Service
public class AuthenticationService {

    // Autowired collega automaticamente i servizi necessari per questa classe
    @Autowired
    private DipendenteService dipendenteService;// Per la gestione dei dipendenti
    @Autowired
    private PasswordEncoder passwordEncoder;// Per cifrare e confrontare le password
    @Autowired
    private ComuneService comuneService;// Per gestire i dati relativi ai comuni
    @Autowired
    private JwtService jwtService;// Per creare e gestire i token JWT per l'autenticazione
    @Autowired
    private AuthenticationManager authenticationManager;// Per autenticare gli utenti
    @Autowired
    private TokenBlackListService tokenBlackListService;// Per aggiungere token alla lista nera durante il logout
    @Autowired
    private JavaMailSender javaMailSender;// Per inviare email

    /**
     * Metodo per registrare un nuovo utente.
     *
     * @param request Oggetto contenente i dati richiesti per la registrazione
     * @return Stringa che conferma l'avvio della registrazione
     */
    public String register(RegisterRequest request) {
        // Crea un nuovo Dipendente con i dati forniti nella richiesta
        Dipendente dipendente = Dipendente
                .builder()
                .nome(request.nome())// Nome
                .cognome(request.cognome())// Cognome
                .email(request.email())//Email
                .avatar(request.avatar())//Avatar(opzionale)
                .dataDiNascita(request.dataDiNascita())//Data di nascita
                .password(passwordEncoder.encode(request.password()))//Cifra la password
                .telefono(request.telefono())//Numero di telefono
                .comuneDiNascita(comuneService.getById(request.comune()))//Recupera il comune di nascita(ID)
                .ruolo(Ruolo.DA_CONFERMARE)//Stato "da confermare" per la registrazione
                .build();
        // Genera un token JWT per la conferma registrazione
        String jwtToken = jwtService.generateToken(dipendente);
        // Salva il token nel campo del dipendente
        dipendente.setRegistrationToken(jwtToken);
        // Salva il dipendente nel database
        dipendenteService.insertDipendente(dipendente);
        // Crea il link di conferma registrazione
        String confirmationUrl = "http://localhost:8080/auth/conferma?token=" + dipendente.getRegistrationToken();
        // Invia una email di conferma
        javaMailSender.send(createConfirmationEmail(dipendente.getEmail(), confirmationUrl));
        AuthenticationResponse.builder().token(jwtToken).build();
        ;
        // Messaggio di conferma registrazione
        return "Dipendente in fase di registrazione, prego confermare la emil!";
    }

    /**
     * Crea una email per confermare la registrazione di un nuovo utente.
     *
     * @param email          Indirizzo dell'utente
     * @param confirmationUrl Link per completare la registrazione
     * @return Una email di tipo SimpleMailMessage pronta per essere inviata
     */
    private SimpleMailMessage createConfirmationEmail(String email, String confirmationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email); // a chi mando la mail
        message.setReplyTo("fcramerotti91@gmail.com"); // Risposta all'indirizzo fornito
        message.setFrom("fcramerotti91@gmail.com"); // da chi viene la mail
        message.setSubject("CONFERMA REGISTRAZIONE, SEMPRE FORZA MAGGGICA");// Oggetto email
        message.setText("Ciao lupacchiotto, clicca qui per essere un vero tifoso DA MAGGGICAAA " + confirmationUrl);// Corpo del messaggio
        return message;
    }

    /**
     * Conferma la registrazione dell'utente basandosi su un token.
     *
     * @param token Token generato per la registrazione
     * @return Risposta generica con messaggio di successo
     */
    public GenericResponse confirmRegistration(String token) {
        // Recupera il dipendente dal token di registrazione
        Dipendente dipendente = dipendenteService.getByRegistrationToken(token);
        // Cambia il ruolo in "UTENTE" dopo la conferma
        dipendente.setRuolo(Ruolo.UTENTE);
        // Aggiorna il database
        dipendenteService.insertDipendente(dipendente);
        return GenericResponse
                .builder()
                .message("Account verificato con successo!")// Messaggio di successo
                .build();
    }

    /**
     * Cambia la password di un utente.
     *
     * @param idDipendente ID del dipendente che vuole aggiornare la password
     * @param request      Oggetto contenente la vecchia e la nuova password
     * @return Risposta (successo o errore) in base all'esito dell'operazione
     */
    public Object changePassword(Long idDipendente, CambiaPasswordRequest request) {
        Dipendente dipendente = dipendenteService.getById(idDipendente);// Recupera il dipendente per ID
        if (!passwordEncoder.matches(request.vecchiaPassword(), dipendente.getPassword())) {
            // Se la vecchia password non coincide, restituisce un errore
            return ErrorResponse
                    .builder()
                    .exception("WrongPasswordException")// Nome dell'eccezione
                    .message("La vecchia password non è corretta")// Messaggio d'errore
                    .build();
        }
        // Aggiorna la password dopo averla cifrata
        dipendente.setPassword(passwordEncoder.encode(request.nuovaPassword()));
        // Salva nel database
        dipendenteService.insertDipendente(dipendente);
        return GenericResponse
                .builder()
                .message("Password cambiata con successo")// Messaggio di successo
                .build();
    }

    /**
     * Autentica un utente sulla base di email e password.
     *
     * @param request Contiene email e password dell'utente
     * @return Risposta di autenticazione contenente il token
     */
    public AuthenticationResponse authenticate(AuthRequest request) {
        // Tenta di autenticare l'utente usando Spring Security
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                // Email (trasformata in minuscolo)
                request.mail().toLowerCase(),
                // Password
                request.password()
        ));
        // Recupera il dipendente tramite email
        Dipendente dipendente = dipendenteService.getByEmail(request.mail());
        // Genera un nuovo token JWT
        String token = jwtService.generateToken(dipendente);
        // Aggiorna l'ultimo login
        dipendente.setLastLogin(LocalDateTime.now());
        // Aggiorna i dati nel database
        dipendenteService.insertDipendente(dipendente);
        // Restituisce il token di autenticazione
        return AuthenticationResponse.builder().token(token).build();
    }

    /**
     * Effettua il logout dell'utente aggiungendo il token alla lista nera.
     *
     * @param idUtente ID dell'utente che esegue il logout
     * @param token    Token fornito nella richiesta
     * @return Messaggio generico che conferma il logout
     */
    public GenericResponse logout(Long idUtente, String token) {
        // Inserisce il token nella lista nera
        tokenBlackListService.insertToken(idUtente, token);
        // Messaggio di successo
        return GenericResponse.builder().message("Logout effettuato con successo").build();
    }
}
