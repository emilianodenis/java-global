package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="PROFESSION")
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

    public Profession(String description) {
        this.description = description;
    }

    public Profession() {

    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
