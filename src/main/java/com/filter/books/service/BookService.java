package com.filter.books.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filter.books.model.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class BookService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy").withZone(ZoneId.systemDefault());

    public Optional<BookDate> filter(String filter) {
        List<Book> books = loadBooksFromFile();
        if (books == null) {
            return Optional.empty();
        }

        // Imprimir libros sin fecha de publicación
        books.stream()
             .filter(book -> book.getPublicationTimestamp() == null)
             .forEach(book -> System.out.println("Book without publication date: " + book.getTitle()));

        // Filtrar libros que contienen la cadena de caracteres en el título, resumen o biografía del autor
        List<Book> filteredBooks = books.stream()
            .filter(book -> book.getTitle().toLowerCase().contains(filter.toLowerCase()) || 
                            book.getSummary().toLowerCase().contains(filter.toLowerCase()) || 
                            book.getAuthor().getBio().toLowerCase().contains(filter.toLowerCase()))
            .collect(Collectors.toList());

        // Ordenar los libros por fecha de publicación (más reciente primero) y luego por longitud de la biografía del autor
        filteredBooks.sort(Comparator.comparing(Book::getPublicationTimestamp, Comparator.nullsFirst(Comparator.naturalOrder()))
                                     .thenComparing(book -> book.getAuthor().getBio().length()));

        // Devolver el libro más reciente con el campo 'date' adicional
        if (!filteredBooks.isEmpty()) {
            Book mostRecentBook = filteredBooks.get(filteredBooks.size() - 1);
            String date = Optional.ofNullable(mostRecentBook.getPublicationTimestamp())
                                  .map(Long::parseLong)
                                  .map(Instant::ofEpochSecond)
                                  .map(DATE_FORMATTER::format)
                                  .orElse(null);
            return Optional.of(new BookDate(mostRecentBook, date));  // Usar ofNullable en lugar de of
        }

        return Optional.empty();
    }

    private List<Book> loadBooksFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = new ClassPathResource("books.json").getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<Book>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
