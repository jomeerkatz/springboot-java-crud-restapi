package com.example.connectToPostgres;

import com.example.connectToPostgres.domain.dto.AuthorDto;
import com.example.connectToPostgres.domain.dto.BookDto;
import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;
import com.example.connectToPostgres.domain.dto.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil(){}

    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
//                .id(1L)
                .age(80)
                .name("Jo meerkatz")
                .build();
    }

    public static AuthorDto createTestAuthorADto() {
        return AuthorDto.builder()
                .id(1L)
                .age(80)
                .name("Jo meerkatz")
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
//                .id(2L)
                .age(44)
                .name("Max Mustermann")
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
//                .id(3L)
                .age(24)
                .name("Gina Krumm")
                .build();
    }

    public static BookEntity createTestBookA(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("isbnTestA")
                .title("tempA")
                .author(author)
                .build();
    }

    public static BookDto createTestBookDtoA(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("isbnTestA")
                .title("tempA")
                .author(authorDto)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("isbnTestB")
                .title("tempB")
                .author(author)
                .build();
    }

    public static BookEntity createTestBookC(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("isbnTestC")
                .title("tempC")
                .author(author)
                .build();
    }
}
