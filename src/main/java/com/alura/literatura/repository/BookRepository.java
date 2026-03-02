package com.alura.literatura.repository;

import com.alura.literatura.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleIgnoreCase(String title);
    @Query("SELECT b FROM Book b JOIN FETCH b.authors")
    List<Book> findAllWithAuthors();
    @EntityGraph(attributePaths = "authors")
    List<Book> findByLanguageIgnoreCase(String language);
}
