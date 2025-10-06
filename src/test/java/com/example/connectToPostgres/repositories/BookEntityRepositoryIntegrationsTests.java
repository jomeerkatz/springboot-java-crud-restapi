package com.example.connectToPostgres.repositories;

import com.example.connectToPostgres.TestDataUtil;
import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;
import com.example.connectToPostgres.domain.dto.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationsTests {
    private BookRepositories underTest;

    @Autowired
    public BookEntityRepositoryIntegrationsTests(BookRepositories underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity author = TestDataUtil.createTestAuthorA();

        BookEntity bookEntity = TestDataUtil.createTestBookA(author);
        BookEntity savedBookEntity = underTest.save(bookEntity);

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity author = TestDataUtil.createTestAuthorA();

        BookEntity bookEntityA = TestDataUtil.createTestBookA(author);
        BookEntity savedBookEntityA = underTest.save(bookEntityA);

        BookEntity bookEntityB = TestDataUtil.createTestBookB(author);
        BookEntity savedBookEntityB = underTest.save(bookEntityB);

        BookEntity bookEntityC = TestDataUtil.createTestBookC(author);
        BookEntity savedBookEntityC = underTest.save(bookEntityC);

        Iterable<BookEntity> result = underTest.findAll();

        assertThat(result).hasSize(3).containsExactly(savedBookEntityA, savedBookEntityB, savedBookEntityC);
    }

    @Test
    public void testThatUpdatesBook(){
        AuthorEntity author = TestDataUtil.createTestAuthorA();

        BookEntity bookEntity = TestDataUtil.createTestBookA(author);

        BookEntity savedBookEntity = underTest.save(bookEntity);

        savedBookEntity.setTitle("UPDATED");

        underTest.save(savedBookEntity);

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookEntity);
    }

    @Test
    public void testThatDeleteBook() {
        AuthorEntity author = TestDataUtil.createTestAuthorA();

        BookEntity bookEntity = TestDataUtil.createTestBookA(author);

        BookEntity savedBookEntity = underTest.save(bookEntity);
        underTest.delete(savedBookEntity);

        Optional<BookEntity> result = underTest.findById(savedBookEntity.getIsbn());

        assertThat(result).isEmpty();
    }

}