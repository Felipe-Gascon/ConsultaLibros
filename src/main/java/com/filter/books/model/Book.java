package com.filter.books.model;


import lombok.Data;

@Data
public class Book {
    private Integer id;
    private String title;
    private Integer pages;
    private String summary;
    private String publicationTimestamp;
    private Author author;
}
