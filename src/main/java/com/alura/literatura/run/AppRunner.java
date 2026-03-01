package com.alura.literatura.run;

import com.alura.literatura.model.Book;
import com.alura.literatura.model.BookData;
import com.alura.literatura.model.GutendexResult;
import com.alura.literatura.model.Person;
import com.alura.literatura.repository.BookRepository;
import com.alura.literatura.repository.PersonRepository;
import com.alura.literatura.service.ApiClient;
import com.alura.literatura.service.ConvertData;

import java.util.List;
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
        System.out.println("Escribe el nombre del libro que desea buscar");
        var bookName = input.nextLine();
        var json = apiClient.getData(URL_BASE + bookName.replace(" ", "%20"));

        GutendexResult result = converter.obtenerDatos(json, GutendexResult.class);
        List<BookData> datos = result.books();
        BookData book = datos.get(0);
        System.out.println("------------------");
        System.out.println(book);
        System.out.println("------------------");
        return book;
    }

    public void bookByTitle(){
        BookData bookData = getBook();
        List<Person> authors = bookData.authors().stream()
                .map(personData -> new Person(personData))
                .collect(Collectors.toList());
        authors.forEach(author -> personRepository.save(author));
        Book book = new Book(bookData, authors);
        bookRepository.save(book);
    }

    public void getRegisteredBooks(){
        List<Book> registeredBooks = bookRepository.findAll();
        registeredBooks.forEach(System.out::println);
    }

    public void getRegisteredAuthors(){
        List<Person> registeredAuthors = personRepository.findAll();
        registeredAuthors.forEach(System.out::println);
    }

    public void getAliveAuthorsByYear(){
        System.out.println("Ingresa el año: ");
        Integer year = input.nextInt();
        input.nextLine();
        List<Person> authors = personRepository.aliveAuthors(year);
        authors.forEach(System.out::println);
    }

    public void getBooksByLanguage(){
        String menu = """
                Ingrese el idioma para buscar los libros:
                
                es- español
                en- inglés
                fr- francés
                pt- portugués
                """;
        String option = input.nextLine();
        System.out.println(menu);
        List<Book> books = bookRepository.findByLanguageIgnoreCase(option);
        books.forEach(System.out::println);
    }
}
