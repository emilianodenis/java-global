package com.example.demo.controller;

import com.example.demo.entity.Profession;
import com.example.demo.entity.ProfessionRepository;
import com.example.demo.model.Action;
import com.example.demo.model.DemoMessage;
import com.example.demo.model.exception.ProfessionNotFoundException;
import com.example.demo.service.queue.Exchange;
import com.example.demo.service.queue.MessagePublisher;
import com.example.demo.service.queue.Queue;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/professions")
public class ProfessionController {

    private final ProfessionRepository professionRepository;
    private final MessagePublisher messagePublisher;

    public ProfessionController(ProfessionRepository professionRepository, MessagePublisher messagePublisher/*RabbitTemplate rabbitTemplate*/) {
        this.professionRepository = professionRepository;
        this.messagePublisher = messagePublisher;
    }

    @GetMapping()
    public List<Profession> getProfessions() {
        var professions = professionRepository.findAll();
        professions.sort(Comparator.comparing(Profession::getDescription, String.CASE_INSENSITIVE_ORDER));
        return professions;
    }

    @GetMapping("/{id}")
    public Profession getProfession(@PathVariable Integer id) {
        return professionRepository.findById(id).orElseThrow(() -> new ProfessionNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Profession replaceProfession(@RequestBody Profession newProfession, @PathVariable Integer id, @RequestParam(name="professionSessionId") String sessionId) {
        return professionRepository.findById(id)
                .map(profession -> {
                    profession.setDescription(newProfession.getDescription());
                    var updated = professionRepository.save(profession);
                    notify(Action.UPDATE, profession, profession.getId(), sessionId);
                    return updated;
                })
                .orElseGet(() -> {
                    var profession = professionRepository.save(newProfession);
                    notify(Action.CREATE, profession, profession.getId(), sessionId);
                    return profession;
                });
    }

    @PostMapping()
    Profession newProfession(@RequestBody Profession newProfession, @RequestParam(name="professionSessionId") String sessionId) {
        var profession = professionRepository.save(newProfession);
        notify(Action.CREATE, profession, profession.getId(), sessionId);
        return profession;
    }

    @DeleteMapping("{id}")
    void deleteProfession(@PathVariable Integer id, @RequestParam(name="professionSessionId") String sessionId) {
        var profession = professionRepository.findById(id);
        if (profession.isPresent()) {
            professionRepository.deleteById(id);
            notify(Action.DELETE, profession.get(), profession.get().getId(), sessionId);
        }
    }

    private void notify(Action action, Profession profession, Integer id, String sessionToExclude) {
        this.messagePublisher.publishSimpleMessage(Exchange.DEFAULT, Queue.PROFESSIONS, null, new DemoMessage<>(action, profession, id, sessionToExclude));
    }
}
