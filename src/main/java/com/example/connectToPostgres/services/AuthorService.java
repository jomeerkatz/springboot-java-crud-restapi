package com.example.connectToPostgres.services;

import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;

import java.util.List;

public interface AuthorService {
    AuthorEntity createAuthor(AuthorEntity author);

    List<AuthorEntity> findAll();
}
