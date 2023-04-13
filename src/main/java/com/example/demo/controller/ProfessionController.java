package com.example.demo.controller;

import com.example.demo.entity.Profession;
import com.example.demo.entity.ProfessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/professions")
public class ProfessionController {

    private final ProfessionRepository professionRepository;

    public ProfessionController(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    @GetMapping()
    public List<Profession> getProfessions() {
        var professions = professionRepository.findAll();
        professions.sort(Comparator.comparing(Profession::getDescription, String.CASE_INSENSITIVE_ORDER));
        return professions;
    }

}
