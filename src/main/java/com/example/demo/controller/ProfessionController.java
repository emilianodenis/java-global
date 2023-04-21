package com.example.demo.controller;

import com.example.demo.entity.Profession;
import com.example.demo.model.Action;
import com.example.demo.model.DemoMessage;
import com.example.demo.service.data.ProfessionDataService;
import com.example.demo.service.queue.Exchange;
import com.example.demo.service.queue.MessagePublisher;
import com.example.demo.service.queue.Queue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/professions")
public class ProfessionController {

    private final ProfessionDataService professionDataService;
    private final MessagePublisher messagePublisher;

    public ProfessionController(ProfessionDataService professionDataService, MessagePublisher messagePublisher) {
        this.professionDataService = professionDataService;
        this.messagePublisher = messagePublisher;
    }

    @GetMapping()
    public List<Profession> getProfessions() {
        return professionDataService.getProfessions();
    }

    @GetMapping("/{id}")
    public Profession getProfession(@PathVariable Integer id) {
        return professionDataService.getProfession(id);
    }

    @PutMapping("/{id}")
    public Profession replaceProfession(@RequestBody Profession profession, @PathVariable Integer id, @RequestParam(name="professionSessionId") String sessionId) {
        var updated = professionDataService.replaceProfession(profession, id);
        notify(Action.UPDATE, updated, updated.getId(), sessionId);
        return updated;
    }

    @PostMapping()
    Profession newProfession(@RequestBody Profession newProfession, @RequestParam(name="professionSessionId") String sessionId) {
        var profession = professionDataService.newProfession(newProfession);
        notify(Action.CREATE, profession, profession.getId(), sessionId);
        return profession;
    }

    @DeleteMapping("{id}")
    void deleteProfession(@PathVariable Integer id, @RequestParam(name="professionSessionId") String sessionId) {
        var deleted = professionDataService.deleteProfession(id);
        deleted.ifPresent(profession -> notify(Action.DELETE, profession, id, sessionId));
    }

    private void notify(Action action, Profession profession, Integer id, String sessionToExclude) {
        this.messagePublisher.publishSimpleMessage(Exchange.DEFAULT, Queue.PROFESSIONS, null, new DemoMessage<>(action, profession, id, sessionToExclude));
    }
}
