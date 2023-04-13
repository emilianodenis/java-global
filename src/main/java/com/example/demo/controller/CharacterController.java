package com.example.demo.controller;

import com.example.demo.entity.BaseCharacter;
import com.example.demo.entity.Character;
import com.example.demo.entity.CharacterRepository;
import com.example.demo.message.queue.MainQueue;
import com.example.demo.model.exception.CharacterNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/characters")
public class CharacterController {

    private final CharacterRepository characterRepository;
    private final RabbitTemplate rabbitTemplate;

    public CharacterController(CharacterRepository characterRepository, RabbitTemplate rabbitTemplate) {
        this.characterRepository = characterRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

//    @GetMapping()
//    public List<Character> getCharacters() {
//        var characters = characterRepository.findAll();
//        characters.sort(Comparator.comparing(Character::getLastName, String.CASE_INSENSITIVE_ORDER).thenComparing(Character::getFirstName, String.CASE_INSENSITIVE_ORDER));
//        return characters;
//    }

    @GetMapping()
    public List<BaseCharacter> getCharacters() {
        return characterRepository.getCharactersSummary();
    }

    @GetMapping("/{id}")
    public Character getCharacter(@PathVariable Integer id) {
        return characterRepository.findById(id).orElseThrow(() -> new CharacterNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Character replaceCharacter(@RequestBody Character newCharacter, @PathVariable Integer id) {
        return characterRepository.findById(id)
                .map(character -> {
                    character.setFirstName(newCharacter.getFirstName());
                    character.setLastName(newCharacter.getLastName());
                    character.setProfession(newCharacter.getProfession());
                    character.setEmail(newCharacter.getEmail());
                    return characterRepository.save(character);
                })
                .orElseGet(() -> characterRepository.save(newCharacter));
    }

    @PostMapping()
    Character newCharacter(@RequestBody Character newCharacter) {
        var character = characterRepository.save(newCharacter);
        notify("New character created: " + character);
        return character;
    }

    @DeleteMapping("{id}")
    void deleteCharacter(@PathVariable Integer id) {
        var character = characterRepository.findById(id);
        if (character.isPresent()) {
            characterRepository.deleteById(id);
        }
    }

    private void notify(String message) {
        var routingKey = MainQueue.baseRoutingKey + "baz";
        var topicExchangeName = MainQueue.topicExchangeName;
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, message);
    }
}
