package com.example.demo.controller;

import com.example.demo.entity.Character;
import com.example.demo.entity.CharacterRepository;
import com.example.demo.model.exception.CharacterNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/characters")
public class CharacterController {

    private final CharacterRepository characterRepository;

    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @GetMapping()
    public List<Character> getCharacters() {
        var characters = characterRepository.findAll();
        characters.sort(Comparator.comparing(Character::getLastName, String.CASE_INSENSITIVE_ORDER).thenComparing(Character::getFirstName, String.CASE_INSENSITIVE_ORDER));
        return characters;
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
                    character.setEmail(newCharacter.getEmail());
                    return characterRepository.save(newCharacter);
                })
                .orElseGet(() -> {
                    newCharacter.setId(id);
                    return characterRepository.save(newCharacter);
                });
    }

    @PostMapping()
    Character newCharacter(@RequestBody Character newCharacter) {
        return characterRepository.save(newCharacter);
    }

    @DeleteMapping("{id}")
    void deleteCharacter(@PathVariable Integer id) {
        var character = characterRepository.findById(id);
        if (character.isPresent()) {
            characterRepository.deleteById(id);
        }
    }
}
