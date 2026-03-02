package com.alura.literatura.repository;

import com.alura.literatura.model.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByNameIgnoreCase(String name);
    @Query("SELECT DISTINCT p FROM Person p JOIN FETCH p.books")
    List<Person> findAllWithBooks();

    @EntityGraph(attributePaths = "books")
    @Query("""
       SELECT p 
       FROM Person p 
       WHERE p.birthYear <= :year 
       AND (p.deathYear IS NULL OR p.deathYear >= :year)
       """)
    List<Person> getAliveAuthorsByYear(@Param("year") Integer year);
}