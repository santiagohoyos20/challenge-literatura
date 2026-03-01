package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PersonData(
        @JsonAlias("birth_year") Integer birthYear,
        @JsonAlias("death_year") Integer deathYear,
        @JsonAlias("name") String name
) {
}