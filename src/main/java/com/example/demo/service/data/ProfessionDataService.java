package com.example.demo.service.data;

import com.example.demo.entity.Profession;
import com.example.demo.entity.ProfessionRepository;
import com.example.demo.model.exception.ProfessionNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessionDataService {

    private final ProfessionRepository professionRepository;

    public ProfessionDataService(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    public List<Profession> getProfessions() {
        var professions = professionRepository.findAll();
        professions.sort(Comparator.comparing(Profession::getDescription, String.CASE_INSENSITIVE_ORDER));
        return professions;
    }

    public Profession getProfession(Integer id) {
        return professionRepository.findById(id).orElseThrow(() -> new ProfessionNotFoundException(id));
    }

    public Profession replaceProfession(Profession newProfession, Integer id) {
        return professionRepository.findById(id)
                .map(profession -> {
                    profession.setDescription(newProfession.getDescription());
                    return professionRepository.save(profession);
                })
                .orElseThrow(() -> new ProfessionNotFoundException(id));
    }

    public Profession newProfession(Profession newProfession) {
        return professionRepository.save(newProfession);
    }
    public Optional<Profession> deleteProfession(Integer id) {
        var profession = professionRepository.findById(id);
        if (profession.isPresent()) {
            professionRepository.deleteById(id);
        }
        return profession;
    }
}
