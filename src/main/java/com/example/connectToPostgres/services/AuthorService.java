package com.example.connectToPostgres.services;

import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;

public interface AuthorService {
    AuthorEntity createAuthor(AuthorEntity author);
}
