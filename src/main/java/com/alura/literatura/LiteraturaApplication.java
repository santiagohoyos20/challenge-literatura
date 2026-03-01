package com.alura.literatura;

import com.alura.literatura.repository.BookRepository;
import com.alura.literatura.repository.PersonRepository;
import com.alura.literatura.run.AppRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PersonRepository personRepository;


    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        AppRunner appRunner = new AppRunner(bookRepository, personRepository);
        appRunner.showMenu();
    }
}
