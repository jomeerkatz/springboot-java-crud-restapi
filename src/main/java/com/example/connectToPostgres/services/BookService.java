package com.example.connectToPostgres.services;

import com.example.connectToPostgres.domain.dto.BookDto;
import com.example.connectToPostgres.domain.dto.entities.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookEntity createUpdateBook(String isbn, BookEntity bookEntity);

    List<BookEntity> findAll();

    Optional<BookEntity> findOne(String isbn);

    Boolean isExists(String isbn);

    BookEntity partialUpdate(String isbn, BookEntity bookEntity);

}
