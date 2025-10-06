package com.example.connectToPostgres.services.impl;

import com.example.connectToPostgres.domain.dto.entities.BookEntity;
import com.example.connectToPostgres.repositories.BookRepositories;
import com.example.connectToPostgres.services.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepositories bookRepositories;

    public BookServiceImpl(BookRepositories bookRepositories) {
        this.bookRepositories = bookRepositories;
    }

    @Override
    public BookEntity createBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepositories.save(bookEntity);
    }
}
