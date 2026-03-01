package com.alura.literatura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    private List<String> languages;
    private Integer downloadCount;
    @ManyToMany()
    @JoinTable(
            name = "book_person",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Person> authors;

    public Book(){}

    public Book(BookData bookData, List<Person> authors) {
        this.title = bookData.title();
        this.languages = bookData.languages();
        this.downloadCount = bookData.downloadCount();
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", languages=" + languages +
                ", downloadCount=" + downloadCount +
                '}';
    }
}
