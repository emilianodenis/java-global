package com.example.demo.controller;

import com.example.demo.entity.BaseCharacter;
import com.example.demo.entity.Character;
import com.example.demo.model.Action;
import com.example.demo.model.DemoMessage;
import com.example.demo.service.data.CharacterDataService;
import com.example.demo.service.queue.Exchange;
import com.example.demo.service.queue.MessagePublisher;
import com.example.demo.service.queue.Queue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/characters")
public class CharacterController {

    private final MessagePublisher messagePublisher;

    private final CharacterDataService characterDataService;

    public CharacterController(MessagePublisher messagePublisher, CharacterDataService characterDataService) {
        this.messagePublisher = messagePublisher;
        this.characterDataService = characterDataService;
    }

//    @GetMapping()
//    public List<Character> getCharacters() {
//        var characters = characterRepository.findAll();
//        characters.sort(Comparator.comparing(Character::getLastName, String.CASE_INSENSITIVE_ORDER).thenComparing(Character::getFirstName, String.CASE_INSENSITIVE_ORDER));
//        return characters;
//    }

    @GetMapping()
    public List<BaseCharacter> getCharacters() {
        return characterDataService.getCharacters();
    }

    @GetMapping("/{id}")
    public Character getCharacter(@PathVariable Integer id) {
        return characterDataService.getCharacter(id);
    }

    @PutMapping("/{id}")
    public Character replaceCharacter(@RequestBody Character character, @PathVariable Integer id, @RequestParam(name="characterSessionId") String sessionId) {
        var updated = characterDataService.replaceCharacter(character, id);
        notify(Action.UPDATE, updated, updated.getId(), sessionId);
        return updated;
    }

    @PostMapping()
    Character newCharacter(@RequestBody Character newCharacter, @RequestParam(name="characterSessionId") String sessionId) {
        var character = characterDataService.newCharacter(newCharacter);
        notify(Action.CREATE, character, character.getId(), sessionId);
        return character;
    }

    @DeleteMapping("{id}")
    void deleteCharacter(@PathVariable Integer id, @RequestParam(name="characterSessionId") String sessionId) {
        var deleted = characterDataService.deleteCharacter(id);
        deleted.ifPresent(character -> notify(Action.DELETE, character, id, sessionId));
    }

    private void notify(Action action, Character character, Integer id, String sessionToExclude) {
        this.messagePublisher.publishSimpleMessage(Exchange.DEFAULT, Queue.CHARACTERS, null, new DemoMessage<>(action, character, id, sessionToExclude));
    }
}
