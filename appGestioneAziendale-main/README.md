# APP DI GESTIONE AZIENDALE

Una famosa azienda ci ha commissionato un’applicazione per la gestione dell’azienda, scaricabile da tutti i dipendenti,
con una serie di funzionalità volte a migliorare la comunicazione e creare uno spazio social tra i lavoratori.

Un dipendente si deve registrare utilizzando i propri dati personali, come:

- Nome
- Cognome
- Email
- Password (servirà per il login)
- Data e luogo di nascita
- Numero di telefono
- Immagine profilo in Base64

La registrazione deve essere confermata tramite l’invio di una email all’indirizzo indicato dall’utente.

I dipendenti possono essere suddivisi in:

- **Dipartimenti** (nome e descrizione)
- **Posizioni lavorative** (che afferiscono direttamente ai dipartimenti, con nome e descrizione)

All’interno dell’app, dove gli utenti sono divisi per ruoli, i publisher possono:

- **Pubblicare delle news e delle comunicazioni aziendali**:
    - **News**:
        - Titolo
        - Contenuto
        - Immagine allegata in Base64
        - Possibilità di mettere like e commentare
        - Possibilità di allegare file
    - **Comunicazioni aziendali**:
        - Solo testo (niente immagini, like o commenti)
        - Possibilità di allegare file

- **Timbratura**:
    - Ogni dipendente può giornalmente inserire:
        - Entrata
        - Inizio pausa pranzo
        - Fine pausa pranzo
        - Uscita

Prossimamente per altre features :P
