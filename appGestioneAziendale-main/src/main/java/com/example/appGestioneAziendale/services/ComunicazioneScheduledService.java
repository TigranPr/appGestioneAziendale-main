package com.example.appGestioneAziendale.services;

import com.example.appGestioneAziendale.domain.dto.requests.ComunicazioneAziendaleRequest;
import com.example.appGestioneAziendale.domain.dto.requests.ComunicazioneScheduledRequest;
import com.example.appGestioneAziendale.domain.dto.requests.ComunicazioneScheduledUpdateRequest;
import com.example.appGestioneAziendale.domain.dto.response.EntityIdResponse;
import com.example.appGestioneAziendale.domain.entities.ComunicazioneScheduled;
import com.example.appGestioneAziendale.domain.entities.Dipendente;
import com.example.appGestioneAziendale.domain.exceptions.MyEntityNotFoundException;
import com.example.appGestioneAziendale.repository.ComunicazioneScheduledRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.SchedulingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ComunicazioneScheduledService implements Job {

    @Autowired
    private ComunicazioneScheduledRepository comunicazioneScheduledRepository;

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private ComunicazioneAziendaleService comunicazioneService;

    @Autowired
    private Scheduler scheduler;

    /**
     * Crea una nuova comunicazione schedulata, salva l'entità nel database e configura il job nel Scheduler.
     *
     * @param request richiesta contenente i dati della comunicazione
     * @return l'ID della comunicazione salvata
     */
    public EntityIdResponse createComunicazioneScheduled(ComunicazioneScheduledRequest request)
            throws MyEntityNotFoundException, SchedulingException, SchedulerException {
        Dipendente dipendente = dipendenteService.getById(request.idDipendente());
        ComunicazioneScheduled comunicazioneScheduled = ComunicazioneScheduled.builder()
                .titolo(request.titolo())
                .testo(request.testo())
                .allegato_url(request.allegato_url())
                .publishTime(request.publishTime())
                .idDipendente(dipendente)
                .build();
        comunicazioneScheduledRepository.save(comunicazioneScheduled);

        // Configura il job
        JobDetail jobDetails = buildJobDetail(comunicazioneScheduled);
        Trigger trigger = buildJobTrigger(jobDetails, convertToDate(comunicazioneScheduled.getPublishTime()));

        scheduler.scheduleJob(jobDetails, trigger);

        return EntityIdResponse.builder().id(comunicazioneScheduled.getId()).build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Date publishTime) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startAt(publishTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();
    }

    private JobDetail buildJobDetail(ComunicazioneScheduled comunicazioneScheduled) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("id", comunicazioneScheduled.getId());
        jobDataMap.put("entityData", comunicazioneScheduled);

        return JobBuilder.newJob(ComunicazioneScheduledService.class)
                .withIdentity(String.valueOf(comunicazioneScheduled.getId()), "comunicazioni")
                .storeDurably()
                .setJobData(jobDataMap)
                .build();
    }

    private Date convertToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * Esegue il lavoro programmato e crea la comunicazione aziendale associata.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Long scheduledId = jobDataMap.getLong("id");

        try {
            // Recupera la comunicazione schedulata
            ComunicazioneScheduled comunicazioneScheduled = comunicazioneScheduledRepository.findById(scheduledId)
                    .orElseThrow(() -> new JobExecutionException("Comunicazione non trovata"));

            Dipendente dipendente = comunicazioneScheduled.getIdDipendente();

            // Crea la comunicazione aziendale
            ComunicazioneAziendaleRequest request = new ComunicazioneAziendaleRequest(
                    comunicazioneScheduled.getTitolo(), comunicazioneScheduled.getTesto(), comunicazioneScheduled.getAllegato_url());

            comunicazioneService.createComunicazioneAziendale(dipendente.getId(), request);

            // Elimina la comunicazione schedulata
            comunicazioneScheduledRepository.deleteById(scheduledId);
        } catch (Exception e) {
            throw new JobExecutionException("Errore durante l'esecuzione della comunicazione", e);
        }
    }

    /**
     * Aggiorna i dettagli di una comunicazione schedulata.
     */
    public EntityIdResponse updateComunicazioneScheduled(Long id, ComunicazioneScheduledUpdateRequest request)
            throws SchedulerException, MyEntityNotFoundException {
        // Recupera la comunicazione dal database
        ComunicazioneScheduled comunicazioneScheduled = comunicazioneScheduledRepository.findById(id)
                .orElseThrow(() -> new MyEntityNotFoundException("Comunicazione non trovata"));

        // Elimina il precedente job
        JobKey jobKey = new JobKey(String.valueOf(comunicazioneScheduled.getId()), "comunicazioni");
        scheduler.deleteJob(jobKey);

        // Aggiorna i dettagli dell'entità
        comunicazioneScheduled.setPublishTime(request.publishTime());
        comunicazioneScheduledRepository.save(comunicazioneScheduled);

        // Ricrea il job per i nuovi dettagli
        return createComunicazioneScheduled(ComunicazioneScheduledRequest.builder()
                .titolo(comunicazioneScheduled.getTitolo())
                .testo(comunicazioneScheduled.getTesto())
                .publishTime(comunicazioneScheduled.getPublishTime())
                .idDipendente(comunicazioneScheduled.getIdDipendente().getId())
                .build());
    }

    /**
     * Elimina una comunicazione schedulata.
     */
    public void deleteComunicazioneScheduledById(Long id) throws SchedulerException, MyEntityNotFoundException {
        // Recupera la comunicazione
        ComunicazioneScheduled comunicazioneScheduled = comunicazioneScheduledRepository.findById(id)
                .orElseThrow(() -> new MyEntityNotFoundException("Comunicazione schedulata con ID " + id + " non trovata"));

        // Rimuove il job dallo scheduler
        JobKey jobKey = new JobKey(String.valueOf(comunicazioneScheduled.getId()), "comunicazioni");
        scheduler.deleteJob(jobKey);

        // Elimina l'entità dal database
        comunicazioneScheduledRepository.deleteById(id);
    }
}