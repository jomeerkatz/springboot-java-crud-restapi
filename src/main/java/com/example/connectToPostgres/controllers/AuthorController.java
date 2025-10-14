package com.example.connectToPostgres.controllers;

import com.example.connectToPostgres.domain.dto.AuthorDto;
import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;
import com.example.connectToPostgres.mappers.Mapper;
import com.example.connectToPostgres.services.AuthorService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorService authorService;

    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    // constructor
    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
    }

    @GetMapping(path="/authors")
    public List<AuthorDto> listAuthors(){
        List<AuthorEntity> authors =  authorService.findAll();
        return authors.stream().map(authorMapper::mapTo).collect(Collectors.toList()); // this is not optimal
    }

    @GetMapping(path="/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Long id ){
        Optional<AuthorEntity> author = authorService.findOne(id);
        return author.map(currentAuthor -> {
            AuthorDto authorDto = authorMapper.mapTo(currentAuthor);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path="/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto){
        if(!authorService.isExisting(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        authorDto.setId(id);
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity updatedAuthorEntity = authorService.save(authorEntity);
        AuthorDto updatedAuthorDto = authorMapper.mapTo(updatedAuthorEntity);

        return new ResponseEntity<>(updatedAuthorDto, HttpStatus.OK);
    }


    @PatchMapping(path="/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdate(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto) {
        if(!authorService.isExisting(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity partialUpdateAuthor = authorService.partialUpdate(id, authorEntity);

        return new ResponseEntity<>(authorMapper.mapTo(partialUpdateAuthor), HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") Long id) {
        authorService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
