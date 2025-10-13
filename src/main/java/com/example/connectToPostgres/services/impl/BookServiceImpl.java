package com.example.connectToPostgres.services.impl;

import com.example.connectToPostgres.domain.dto.BookDto;
import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;
import com.example.connectToPostgres.domain.dto.entities.BookEntity;
import com.example.connectToPostgres.repositories.BookRepositories;
import com.example.connectToPostgres.services.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private BookRepositories bookRepositories;

    public BookServiceImpl(BookRepositories bookRepositories) {
        this.bookRepositories = bookRepositories;
    }

    @Override
    public BookEntity createUpdateBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepositories.save(bookEntity);
    }

    @Override
    public List<BookEntity> findAll() {
        return StreamSupport
                .stream(bookRepositories.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookRepositories.findById(isbn);
    }

    @Override
    public Boolean isExists(String isbn) {
        return bookRepositories.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepositories.findById(isbn).map(existingBook -> {
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
            return bookRepositories.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Book does not exists!"));
    }
}
