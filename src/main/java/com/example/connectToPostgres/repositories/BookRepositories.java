package com.example.connectToPostgres.repositories;

import com.example.connectToPostgres.domain.dto.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookRepositories extends CrudRepository<BookEntity, String> {
}
