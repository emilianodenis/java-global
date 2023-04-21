package com.example.demo.service.data;

import com.example.demo.entity.BaseCharacter;
import com.example.demo.entity.Character;
import com.example.demo.entity.CharacterRepository;
import com.example.demo.model.exception.CharacterNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterDataService {

    private final CharacterRepository characterRepository;

    public CharacterDataService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public List<BaseCharacter> getCharacters() {
        return characterRepository.getCharactersSummary();
    }

    public Character getCharacter(Integer id) {
        return characterRepository.findById(id).orElseThrow(() -> new CharacterNotFoundException(id));
    }

    public Character replaceCharacter(Character newCharacter, Integer id) {
        return characterRepository.findById(id)
                .map(character -> {
                    character.setFirstName(newCharacter.getFirstName());
                    character.setLastName(newCharacter.getLastName());
                    character.setProfession(newCharacter.getProfession());
                    character.setEmail(newCharacter.getEmail());
                    return characterRepository.save(character);
                })
                .orElseThrow(() -> new CharacterNotFoundException(id));
    }

    public Character newCharacter(Character newCharacter) {
        return characterRepository.save(newCharacter);
    }
    public Optional<Character> deleteCharacter(Integer id) {
        var character = characterRepository.findById(id);
        if (character.isPresent()) {
            characterRepository.deleteById(id);
        }
        return character;
    }
}
