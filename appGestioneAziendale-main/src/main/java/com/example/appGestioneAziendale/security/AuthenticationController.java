package com.example.appGestioneAziendale.security;

import com.example.appGestioneAziendale.domain.dto.requests.AuthRequest;
import com.example.appGestioneAziendale.domain.dto.requests.CambiaPasswordRequest;
import com.example.appGestioneAziendale.domain.dto.requests.RegisterRequest;
import com.example.appGestioneAziendale.domain.dto.response.AuthenticationResponse;
import com.example.appGestioneAziendale.domain.dto.response.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Indica che questa classe è un controller REST, cioè risponde a richieste HTTP (es. POST, GET, ecc.)
@RestController
// Specifica il percorso base per tutte le richieste che questa classe gestisce (es. /auth/register)
@RequestMapping("/auth")
public class AuthenticationController {

    // Collegamento automatico al servizio di autenticazione (dove è implementata la logica)
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Endpoint per la registrazione di un nuovo utente
     * Metodo: POST
     * Percorso: /auth/register
     * Corpo della richiesta: Oggetto `RegisterRequest`, validato con @Valid
     * Risposta: Stringa che conferma la registrazione con codice HTTP 201 (CREATED)
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        // Chiama il servizio per registrare il nuovo utente e restituisce la risposta
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    /**
     * Endpoint per effettuare il login
     * Metodo: POST
     * Percorso: /auth/login
     * Corpo della richiesta: Oggetto `AuthRequest`, validato con @Valid
     * Risposta: Oggetto `AuthenticationResponse` (es. token di autenticazione), con codice HTTP 201 (CREATED)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthRequest request) {
        // Chiama il servizio per autenticare l'utente e restituisce i dati di autenticazione (es. token)
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.CREATED);
    }

    /**
     * Endpoint per effettuare il logout di un utente
     * Metodo: POST
     * Percorso: /auth/logout/{id_utente}
     * Parametro di percorso: `id_utente` (ID dell'utente che si vuole disconnettere)
     * Header della richiesta: Token di autorizzazione (Authorization)
     * Risposta: Oggetto `GenericResponse` con un messaggio di successo o errore, con codice HTTP 201 (CREATED)
     */
    @PostMapping("/logout/{id_utente}")
    public ResponseEntity<GenericResponse> logout(@PathVariable Long id_utente, HttpServletRequest request) {
        // Estrae il token dalla richiesta HTTP (rimuovendo il prefisso "Bearer ")
        String token = request.getHeader("Authorization").substring(7);
        // Chiama il servizio per eseguire il processo di logout e restituisce la risposta
        return new ResponseEntity<>(authenticationService.logout(id_utente, token), HttpStatus.CREATED);
    }

    /**
     * Endpoint per confermare la registrazione di un utente tramite token
     * Metodo: GET
     * Percorso: /auth/conferma
     * Parametro della richiesta: `token` (fornito tramite query string, es. /auth/conferma?token=...)
     * Risposta: Oggetto `GenericResponse` che conferma o segnala un errore nella registrazione, con codice HTTP 201 (CREATED)
     */
    @GetMapping("/conferma")
    public ResponseEntity<GenericResponse> confirmRegistration(@RequestParam String token) {
        // Chiama il servizio per confermare la registrazione legata al token
        return new ResponseEntity<>(authenticationService.confirmRegistration(token), HttpStatus.CREATED);
    }

    /**
     * Endpoint per cambiare la password di un utente
     * Metodo: POST
     * Percorso: /auth/cambia_password/{id_utente}
     * Parametro di percorso: `id_utente` (ID del dipendente che vuole cambiare la password)
     * Corpo della richiesta: Oggetto `CambiaPasswordRequest` con le informazioni necessarie
     * Risposta:
     * - Codice HTTP 201 (CREATED) se il cambio di password è avvenuto con successo.
     * - Codice HTTP 403 (FORBIDDEN) se c'è stato un problema (es. la vecchia password non coincide).
     */
    @PostMapping("/cambia_password/{id_utente}")
    public ResponseEntity<?> changePassword(@PathVariable Long idDipendente, @RequestBody CambiaPasswordRequest request) {
        // Chiama il servizio per cambiare la password e riceve un risultato
        Object result = authenticationService.changePassword(idDipendente, request);
        // Verifica se il risultato è un messaggio generico di successo (GenericResponse)
        if (result.getClass() == GenericResponse.class) {
            // Restituisce il risultato con HTTP 201
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        // In caso contrario, restituisce HTTP 403 (accesso negato o problema convalidazione)
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }
}
