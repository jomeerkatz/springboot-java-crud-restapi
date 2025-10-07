package com.example.connectToPostgres.services.impl;

import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;
import com.example.connectToPostgres.repositories.AuthorRepositories;
import com.example.connectToPostgres.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepositories authorRepositories;

    public AuthorServiceImpl (AuthorRepositories authorRepositories) {
        this.authorRepositories = authorRepositories;
    }

    @Override
    public AuthorEntity createAuthor(AuthorEntity author) {
        return authorRepositories.save(author);
    }

    @Override
    public List<AuthorEntity> findAll() {
       return StreamSupport.stream(authorRepositories.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
