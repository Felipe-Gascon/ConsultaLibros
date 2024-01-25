package com.filter.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.filter.books.model.*;
import com.filter.books.service.BookService;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/filter")
    public ResponseEntity<BookDate> filterBooks(@RequestParam String filter) {
        return bookService.filter(filter)
                .map(ResponseEntity::ok) // si se encuentra un libro, devuelve 200 OK con los datos del libro
                .orElseGet(() -> ResponseEntity.noContent().build()); // si no se encuentra ningún libro, devuelve 204 No Content
    }
}
