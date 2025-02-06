package com.example.appGestioneAziendale.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * Configura la sicurezza dell'applicazione utilizzando Spring Security.
 * Gestisce l'autenticazione e l'autorizzazione per richieste HTTP.
 */
@Configuration // Indica che questa classe contiene configurazioni Spring.
@EnableWebSecurity // Abilita il modulo di sicurezza di Spring Security nell'applicazione.
public class SecurityConfig {

    // Filtro personalizzato per intercettare e convalidare i token JWT nelle richieste HTTP.
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // Provider di autenticazione che gestisce la logica di validazione degli utenti.
    @Autowired
    private AuthenticationProvider authenticationProvider;

    /**
     * Configura la sicurezza attraverso la definizione di una catena di filtri di sicurezza.
     * Questo metodo centrale personalizza gli aspetti di autenticazione e autorizzazione dell'app.
     *
     * @param http Oggetto per configurare la sicurezza HTTP.
     * @return La catena di filtri di sicurezza configurata.
     * @throws Exception Se si verifica un errore durante la configurazione.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Definizione degli endpoint pubblici. Sono accessibili senza autenticazione.
        List<String> publicEndpoints = List.of(
                "/auth/register", // Endpoint per la registrazione di nuovi utenti.
                "/auth/login",    // Endpoint per l'accesso e la generazione di token JWT.
                "/start-db",      // Endpoint ipotetico per avviare il database.
                "/comuni/create"  // Endpoint ipotetico per creare comuni.
        );

        // Imposta gli endpoint pubblici nel filtro JWT. Questo consente al filtro di non elaborare richieste verso questi endpoint.
        jwtAuthFilter.setPublicEndpoints(publicEndpoints);

        // Configura la sicurezza HTTP.
        http.csrf(AbstractHttpConfigurer::disable) // Disabilita la protezione CSRF (non necessaria per le API REST protette da JWT).
                .authorizeHttpRequests(req -> req
                        .requestMatchers(publicEndpoints.toArray(new String[0])) // Per gli endpoint pubblici
                        .permitAll() // Consente l'accesso senza autenticazione.
                        .anyRequest() // Qualsiasi altra richiesta
                        .authenticated() // Richiede l'autenticazione.
                )
                .authenticationProvider(authenticationProvider) // Configura il provider di autenticazione.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Aggiunge il filtro JWT prima del filtro standard.

        // Restituisce la catena di filtri di sicurezza configurata.
        return http.build();
    }
}