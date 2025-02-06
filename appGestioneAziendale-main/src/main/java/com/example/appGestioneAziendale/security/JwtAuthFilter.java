package com.example.appGestioneAziendale.security;

import com.example.appGestioneAziendale.domain.dto.response.ErrorResponse;
import com.example.appGestioneAziendale.services.TokenBlackListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Questa classe rappresenta un filtro personalizzato per gestire l'autenticazione tramite JWT.
// Estende OncePerRequestFilter, garantendo che il filtro venga eseguito una sola volta per ogni richiesta.
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Servizio per la gestione dei token JWT (es. estrazione username)
    @Autowired
    private JwtService jwtService;

    // Servizio per caricare i dettagli dell'utente, ad esempio con email e ruoli
    @Autowired
    private UserDetailsService userDetailsService;

    // Servizio per verificare se un token è nella blacklist (ad esempio se l'utente ha effettuato il logout)
    @Autowired
    private TokenBlackListService tokenBlackListService;

    // Elenco degli endpoint che non richiedono autenticazione (es. pubblici)
    @Setter
    private List<String> publicEndpoints;

    /**
     * Metodo principale del filtro, eseguito automaticamente per ogni richiesta.
     * Filtra ed elabora eventuali token JWT per l'autenticazione, o restituisce un errore in caso di problemi.
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {

        // Ottiene l'header di autorizzazione dalla richiesta (es. Authorization: Bearer <token>)
        final String authHeader = request.getHeader("Authorization");
        final String jwt; // Memorizza il token JWT
        String email = null; // Email estratta dal token
        final String requestURI = request.getRequestURI(); // Indirizzo richiesto

        // Controlla se l'endpoint richiesto è pubblico (se sì, prosegue senza autenticazione)
        if (isPublic(requestURI)) {
            filterChain.doFilter(request, response); // Passa la richiesta al filtro successivo
            return;
        }

        // Verifica che l'header di autorizzazione esista e inizi correttamente con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Invia una risposta di errore per token mancanti o malformati
            sendAuthErrorResponse(response, "MalformedTokenException", "Token JWT mancante o malformato");
            return;
        }

        // Estrae il token JWT dall'header (rimuovendo il prefisso "Bearer ")
        jwt = authHeader.substring(7);

        // Controlla se il token è presente nella blacklist (es. se scaduto o invalidato)
        if (tokenBlackListService.isPresentToken(jwt)) {
            sendAuthErrorResponse(response, "TokenExpiredException", "Token nella blacklist, non è più valido!");
            return;
        }

        // Estrae l'email (username) dal token. Se il token è malformato, lancia un'eccezione.
        email = jwtService.extractUsername(jwt);

        try {
            // Verifica che l'email non sia null e che non ci sia già un'autenticazione valida nel contesto
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carica i dettagli dell'utente in base alla sua email
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Crea un oggetto di autenticazione contenente l'utente e i suoi ruoli
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Password non necessaria nella SecurityContext
                        userDetails.getAuthorities() // Ruoli/Permessi dell'utente
                );

                // Associa la richiesta corrente ai dettagli
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Salva l'oggetto di autenticazione nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Verifica se l'utente ha il ruolo "TOCONFIRM" (registrato ma non confermato)
                if (userDetails.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("TOCONFIRM"))) {
                    // Se sì, invia un errore di accesso negato
                    sendAuthErrorResponse(response, "AccessDeniedException", "Devi confermare l'account!");
                    return;
                }
            }
        } catch (MalformedJwtException e) {
            // Gestisce errori relativi al token malformato
            sendAuthErrorResponse(response, "MalformedTokenException", "Token JWT mancante o malformato");
            return;
        } catch (UsernameNotFoundException e) {
            // Gestisce errori in cui l'utente non viene trovato
            sendAuthErrorResponse(response, "UsernameNotFoundException", "Username non trovato");
            return;
        }

        // Prosegue con il filtro successivo nella catena se tutto va bene
        filterChain.doFilter(request, response);
    }

    /**
     * Metodo per inviare una risposta di errore quando l'autenticazione fallisce.
     *
     * @param response Oggetto HTTP per inviare la risposta
     * @param error    Tipo di errore riscontrato
     * @param message  Messaggio descrittivo dell'errore
     */
    private void sendAuthErrorResponse(HttpServletResponse response,
                                       String error,
                                       String message) throws IOException {
        // Imposta lo stato della risposta come "401 UNAUTHORIZED"
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Crea un oggetto ErrorResponse con i dettagli dell'errore
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .exception(error) // Nome dell'eccezione
                .message(message) // Messaggio descrittivo dell'errore
                .build();

        // Converte la risposta in formato JSON e la scrive nel body della risposta HTTP
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    /**
     * Verifica se un endpoint è pubblico (non richiede autenticazione).
     *
     * @param requestURI URI della richiesta ricevuta
     * @return `true` se l'endpoint è pubblico, `false` altrimenti
     */
    private boolean isPublic(String requestURI) {
        // Verifica se l'URI richiesto corrisponde a uno degli endpoint pubblici (es. "/auth/**")
        return publicEndpoints.stream()
                .map(endPoint -> endPoint.replace("**", ".*")) // Sostituisce il wildcard con un'espressione regolare
                .anyMatch(requestURI::matches); // Controlla se matcha con l'URI richiesto
    }
}