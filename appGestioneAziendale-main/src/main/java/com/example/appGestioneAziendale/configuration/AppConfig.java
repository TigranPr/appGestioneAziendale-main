package com.example.appGestioneAziendale.configuration;

import com.example.appGestioneAziendale.repository.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

// Indica che questa classe contiene configurazioni per l'applicazione
@Configuration
public class AppConfig {
    
    // Collega automaticamente il repository dei dipendenti per usarlo nelle funzioni successive
    @Autowired
    private DipendenteRepository dipendenteRepository;

    // Configurazione per inviare email
    @Bean
    public JavaMailSender javaMailSender() {

        // Crea un'implementazione del servizio di invio email
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Configura il server SMTP (in questo caso Gmail)
        mailSender.setHost("smtp.gmail.com");
        // Porta per il protocollo SMTP
        mailSender.setPort(25);
        // Email da cui verrà inviato
        mailSender.setUsername("fcramerotti91@gmail.com");
        // Password dell'account (placeholder)
        mailSender.setPassword("zosf jmyt ajqe tzjj");

        // Configura parametri aggiuntivi del servizio email
        Properties properties = mailSender.getJavaMailProperties();
        // Protocollo di trasporto
        properties.put("mail.transport.protocol", "smtp");
        // Richiede autenticazione
        properties.put("mail.smtp.auth", "true");
        // Abilita il protocollo di crittografia STARTTLS
        properties.put("mail.smtp.starttls.enable", "true");
        // Modalità di debug per controllare i dettagli di invio
        properties.put("mail.debug", "true");
        // Ritorna l'oggetto configurato
        return mailSender;
    }

    // Configurazione del servizio per caricare i dettagli degli utenti
    @Bean
    public UserDetailsService userDetailsService(){
        // Implementazione del servizio che cerca un utente nel database per email
        return username -> dipendenteRepository.findByEmail(username).orElseThrow(()-> new RuntimeException("Utente con questa mail non trovato"));
    }

    // Configurazione dell'autenticazione basata su utenti e password
    @Bean
    public AuthenticationProvider authenticationProvider(){
        // Crea un provider di autenticazione basato su DAO (Data Access Object)
        DaoAuthenticationProvider autProvider = new DaoAuthenticationProvider();
        // Imposta il servizio che fornisce i dettagli degli utenti (email, password, ruoli)
        autProvider.setUserDetailsService(userDetailsService());
        // Imposta il meccanismo di codifica/decodifica delle password
        autProvider.setPasswordEncoder(passwordEncoder());
        // Ritorna il provider configurato
        return autProvider;
    }

    // Configurazione del sistema di codifica delle password
    @Bean
    public PasswordEncoder passwordEncoder(){
        // Utilizza l'algoritmo Bcrypt per proteggere le password
        return new BCryptPasswordEncoder();
    }

    // Configurazione del gestore generale dell'autenticazione
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        // Ottiene e configura un gestore per coordinare tutti i processi di autenticazione
        return authenticationConfiguration.getAuthenticationManager();
    }
}