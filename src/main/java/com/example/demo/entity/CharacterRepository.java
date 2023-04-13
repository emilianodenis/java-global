package com.example.demo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    enum FIELD_RANK{
        ID(0),
        FIRST_NAME(1),
        LAST_NAME(2);

        public final Integer value;

        FIELD_RANK(Integer value){
            this.value = value;
        }
    }

    @Query(value = "SELECT id, first_name, last_name FROM Character ORDER BY last_name, first_name", nativeQuery = true)
    List<Object[]> listAll();

    default List<BaseCharacter> getCharactersSummary() {
        return listAll()
                .stream()
                .map(arr -> new BaseCharacter(
                        (Integer) arr[FIELD_RANK.ID.value],
                        (String) arr[FIELD_RANK.FIRST_NAME.value],
                        (String) arr[FIELD_RANK.LAST_NAME.value])

                ).toList();
    }
}
