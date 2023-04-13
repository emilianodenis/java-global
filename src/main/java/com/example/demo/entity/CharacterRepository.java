package com.example.demo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    int ID_RANK = 0;
    int FIRST_NAME_RANK = 1;
    int LAST_NAME_RANK = 2;

    @Query(value = "SELECT id, first_name, last_name FROM Character ORDER BY last_name, first_name", nativeQuery = true)
    List<Object[]> listAll();
}
