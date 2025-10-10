package com.example.connectToPostgres.controllers;

import com.example.connectToPostgres.domain.dto.BookDto;
import com.example.connectToPostgres.domain.dto.entities.BookEntity;
import com.example.connectToPostgres.mappers.Mapper;
import com.example.connectToPostgres.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private Mapper<BookEntity, BookDto> bookMapper;

    private BookService bookService;

    public BookController(Mapper<BookEntity, BookDto> bookMapper, BookService bookService){
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> creatUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean bookExists = bookService.isExists(isbn);
        BookEntity savedBookEntity = bookService.createUpdateBook(isbn, bookEntity);
        BookDto savedBookDto = bookMapper.mapTo(savedBookEntity);

        if(bookExists) {
            return new ResponseEntity<>(savedBookDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(bookDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path="/books")
    public List<BookDto> listBooks(){
        List<BookEntity>  listBookEntity = bookService.findAll();
        return listBookEntity.stream()
                .map(bookMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path="/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {
        // get a specific boook
        Optional<BookEntity> book = bookService.findOne(isbn);
        return book.map( currentBook -> {
            BookDto bookDto = bookMapper.mapTo(currentBook);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
