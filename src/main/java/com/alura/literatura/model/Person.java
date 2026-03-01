package com.alura.literatura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Integer birthYear;
    private Integer deathYear;
    private String name;
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

    public Person(){}

    public Person(PersonData personData) {
        this.name = personData.name();
        this.birthYear = personData.birthYear();
        this.deathYear = personData.deathYear();
    }

    @Override
    public String toString() {
        return "Person{" +
                "birthYear=" + birthYear +
                ", deathYear=" + deathYear +
                ", name='" + name + '\'' +
                '}';
    }
}
