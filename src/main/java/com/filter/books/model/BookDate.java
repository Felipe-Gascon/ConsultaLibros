package com.filter.books.model;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDate {
    Book book;
    String date;
}
