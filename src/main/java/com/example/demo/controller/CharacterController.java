package com.example.demo.controller;

import com.example.demo.entity.BaseCharacter;
import com.example.demo.entity.Character;
import com.example.demo.entity.CharacterRepository;
import com.example.demo.model.exception.CharacterNotFoundException;
import com.example.demo.service.queue.Exchange;
import com.example.demo.service.queue.MessagePublisher;
import com.example.demo.service.queue.Queue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/characters")
public class CharacterController {

    private final CharacterRepository characterRepository;
    private final MessagePublisher messagePublisher;

    public CharacterController(CharacterRepository characterRepository, MessagePublisher messagePublisher/*RabbitTemplate rabbitTemplate*/) {
        this.characterRepository = characterRepository;
        this.messagePublisher = messagePublisher;
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
                    var updated = characterRepository.save(character);
                    notify("character updated: " + updated);
                    return updated;
                })
                .orElseGet(() -> {
                    var character = characterRepository.save(newCharacter);
                    notify("New character created: " + character);
                    return character;
                });
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
            notify("character deleted: " + character);
        }
    }

    private void notify(String message) {
        this.messagePublisher.publishSimpleString(Exchange.DEFAULT, Queue.CHARACTERS, null, message);
    }
}
