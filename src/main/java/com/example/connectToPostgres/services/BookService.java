package com.example.connectToPostgres.services;

import com.example.connectToPostgres.domain.dto.entities.BookEntity;

public interface BookService {

    BookEntity createBook(String isbn, BookEntity bookEntity);
}
