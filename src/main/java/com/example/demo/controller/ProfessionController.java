package com.example.demo.controller;

import com.example.demo.entity.Profession;
import com.example.demo.entity.ProfessionRepository;
import com.example.demo.message.queue.MainQueue;
import com.example.demo.model.exception.ProfessionNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/professions")
public class ProfessionController {

    private final ProfessionRepository professionRepository;
    private final RabbitTemplate rabbitTemplate;

    public ProfessionController(ProfessionRepository professionRepository, RabbitTemplate rabbitTemplate) {
        this.professionRepository = professionRepository;
        this.rabbitTemplate = rabbitTemplate;
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
    public Profession replaceProfession(@RequestBody Profession newProfession, @PathVariable Integer id) {
        return professionRepository.findById(id)
                .map(profession -> {
                    profession.setDescription(newProfession.getDescription());
                    return professionRepository.save(profession);
                })
                .orElseGet(() -> professionRepository.save(newProfession));
    }

    @PostMapping()
    Profession newProfession(@RequestBody Profession newProfession) {
        var profession = professionRepository.save(newProfession);
        notify("New profession created: " + profession);
        return profession;
    }

    @DeleteMapping("{id}")
    void deleteProfession(@PathVariable Integer id) {
        var profession = professionRepository.findById(id);
        if (profession.isPresent()) {
            professionRepository.deleteById(id);
        }
    }

    private void notify(String message) {
        var routingKey = MainQueue.baseRoutingKey + "baz";
        var topicExchangeName = MainQueue.topicExchangeName;
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, message);
    }

}
