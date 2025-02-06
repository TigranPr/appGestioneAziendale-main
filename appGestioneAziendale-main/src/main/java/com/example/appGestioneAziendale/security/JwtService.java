package com.example.appGestioneAziendale.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Indica che questa classe è un servizio gestito da Spring.
@Service
public class JwtService {

    // Chiave segreta per effettuare la firma/validazione dei token JWT.
    // Il valore è configurato nel file `application.properties`.
    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    /**
     * Genera un token JWT a partire dai dettagli di un utente.
     *
     * @param userDetails Contiene i dati dell'utente (es. username, ruoli).
     * @return Token JWT creato con i dati dell'utente.
     */
    public String generateToken(UserDetails userDetails) {
        // Passa una mappa vuota per eventuali claims extra.
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT includendo eventuali "claims" extra forniti dall'applicazione.
     *
     * @param extraClaims  Claims aggiuntivi da includere nel token.
     * @param userDetails  Dettagli dell'utente (es. username).
     * @return Token JWT creato.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder() // Inizia la costruzione del token JWT.
                .setClaims(extraClaims) // Imposta i claims forniti.
                .setSubject(userDetails.getUsername()) // Aggiunge il nome utente come "soggetto" del token.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Timestamp della creazione del token.
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Fissa la scadenza (1 ora in questo caso).
                .signWith(getSingInKey(), SignatureAlgorithm.HS256) // Firma il token con la chiave e l'algoritmo HS256.
                .compact(); // Converte tutto in una stringa JWT compatta e pronta per l'uso.
    }

    /**
     * Recupera la chiave utilizzata per firmare i token JWT.
     *
     * @return La chiave crittografica derivata dalla parola segreta codificata in Base64.
     */
    private Key getSingInKey() {
        // Decodifica la chiave segreta da Base64 in un array di byte.
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        System.out.println(SECRET_KEY); // DEBUG: Consente di verificare il valore reale della chiave (va rimosso in produzione).

        // Crea la chiave HMAC (SHA-256) dalla chiave decodificata.
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Estrae lo "username" (subject) da un token JWT.
     *
     * @param token Token JWT ricevuto.
     * @return Username estratto dal token.
     */
    public String extractUsername(String token) {
        // Utilizza una funzione generica per estrarre il "subject" dai claims del token.
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Estrae la data di scadenza da un token JWT.
     *
     * @param token Token JWT.
     * @return Data di scadenza del token.
     */
    public Date extractExpirationDate(String token) {
        // Utilizza una funzione generica per estrarre il campo `expiration` dai claims.
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Metodo generico per estrarre un qualunque claim dal token JWT.
     *
     * @param token          Token JWT.
     * @param claimsResolver Funzione che elabora i claims JWT (es. `getSubject`, `getExpiration`).
     * @return Il valore del claim estratto.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Estrae tutti i claims disponibili nel token e applica la funzione risolutrice.
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claims (informazioni) contenuti nel token JWT.
     *
     * @param token Token JWT ricevuto.
     * @return Oggetto `Claims` che rappresenta tutte le informazioni contenute nel token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder() // Inizia il parsing del token.
                .setSigningKey(getSingInKey()) // Imposta la chiave con cui validare la firma del token.
                .build() // Costruisce il parser.
                .parseClaimsJws(token) // Verifica e analizza il token JWT.
                .getBody(); // Restituisce il payload contenente le informazioni.
    }

}