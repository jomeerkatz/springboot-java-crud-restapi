package com.example.connectToPostgres.services;

import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorEntity save(AuthorEntity author);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOne(Long id);

    boolean isExisting(Long id);

    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);

}
