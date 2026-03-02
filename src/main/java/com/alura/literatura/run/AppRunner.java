package com.alura.literatura.run;

import com.alura.literatura.model.*;
import com.alura.literatura.repository.BookRepository;
import com.alura.literatura.repository.PersonRepository;
import com.alura.literatura.service.ApiClient;
import com.alura.literatura.service.ConvertData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AppRunner {

    private Scanner input = new Scanner(System.in);
    private ApiClient apiClient = new ApiClient();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvertData converter = new ConvertData();
    private BookRepository bookRepository;
    private PersonRepository personRepository;

    public AppRunner(BookRepository bookRepository,
                     PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public void showMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    _____________________________________________
                    Elija la opción a través de su número:
                    1- buscar libro por título
                    2- listar libros registrados
                    3- listar autores registrados
                    4- listar autores vivos en un determinado año
                    5- listar libros por idioma
                    0 - salir
                    """;
            System.out.println(menu);
            opcion = input.nextInt();
            input.nextLine();

            switch (opcion) {
                case 1:
                    bookByTitle();
                    break;
                case 2:
                    getRegisteredBooks();
                    break;
                case 3:
                    getRegisteredAuthors();
                    break;
                case 4:
                    getAliveAuthorsByYear();
                    break;
                case 5:
                    getBooksByLanguage();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    public BookData getBook(){
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var bookName = input.nextLine();
        var json = apiClient.getData(URL_BASE + bookName.replace(" ", "%20"));

        GutendexResult result = converter.obtenerDatos(json, GutendexResult.class);
        List<BookData> datos = result.books();
        BookData book = datos.get(0);

        String author = "";
        if (book.authors().isEmpty()){
            author = "Desconocido";
        }else {
            author = book.authors().get(0).name();
        }

        String output = """
                ----- LIBRO -----
                Titulo: %s
                Autor: %s
                Idioma: %s
                Numero de descargas: %d
                -----------------
                """.formatted(book.title(), author, book.languages().get(0), book.downloadCount());
        System.out.println(output);
        return book;
    }

    public void bookByTitle(){
        BookData bookData = getBook();

        Optional<Book> existingBook = bookRepository
                .findByTitleIgnoreCase(bookData.title());

        if (existingBook.isPresent()) {
            System.out.println("El libro ya existe en la base de datos");
            return;
        }

        List<Person> authors = Optional.ofNullable(bookData.authors())
                .filter(list -> !list.isEmpty())
                .orElse(List.of(new PersonData(null, null, "Desconocido")))
                .stream()
                .map(personData -> {

                    // Buscar si ya existe el autor
                    return personRepository
                            .findByNameIgnoreCase(personData.name())
                            .orElseGet(() -> personRepository.save(new Person(personData)));

                })
                .collect(Collectors.toList());


        Book book = new Book(bookData, authors);
        bookRepository.save(book);
        System.out.println("Libro guardado correctamente");
    }

    public void getRegisteredBooks(){
        List<Book> registeredBooks = bookRepository.findAllWithAuthors();
        registeredBooks.forEach(book ->
                System.out.printf("""
                ----- LIBRO -----
                Titulo: %s
                Autor: %s
                Idioma: %s
                Numero de descargas: %d
                -----------------
                """.formatted(
                        book.getTitle(),
                        book.getAuthors().get(0).getName(),
                        book.getLanguage(),
                        book.getDownloadCount()
                )));
    }

    public void getRegisteredAuthors(){
        List<Person> registeredAuthors = personRepository.findAllWithBooks();
        registeredAuthors.forEach(person -> {
                String titles = person.getBooks()
                .stream()
                .map(book -> book.getTitle())
                .collect(Collectors.joining(", "));

                System.out.printf("""
                Autor: %s
                Fecha de nacimiento: %d
                Fecha de fallecimiento: %d
                Libro: %s
                -----------------
                """.formatted(
                        person.getName(),
                        person.getBirthYear(),
                        person.getDeathYear(),
                        titles
                ));
        });
    }

    public void getAliveAuthorsByYear(){
        System.out.println("Ingresa el año: ");
        Integer year = input.nextInt();
        input.nextLine();
        List<Person> authors = personRepository.getAliveAuthorsByYear(year);
        authors.forEach(person -> {
            String titles = person.getBooks()
                    .stream()
                    .map(book -> book.getTitle())
                    .collect(Collectors.joining(", "));

            System.out.printf("""
                Autor: %s
                Fecha de nacimiento: %d
                Fecha de fallecimiento: %d
                Libro: %s
                -----------------
                """.formatted(
                    person.getName(),
                    person.getBirthYear(),
                    person.getDeathYear(),
                    titles
            ));
        });
    }

    public void getBooksByLanguage(){
        String menu = """
                Ingrese el idioma para buscar los libros:
                
                es- español
                en- inglés
                fr- francés
                pt- portugués
                """;
        System.out.println(menu);
        String option = input.nextLine();
        List<Book> books = bookRepository.findByLanguageIgnoreCase(option);
        books.forEach(book ->
                System.out.printf("""
                ----- LIBRO -----
                Titulo: %s
                Autor: %s
                Idioma: %s
                Numero de descargas: %d
                -----------------
                """.formatted(
                        book.getTitle(),
                        book.getAuthors().get(0).getName(),
                        book.getLanguage(),
                        book.getDownloadCount()
                )));
    }
}
