package com.example.connectToPostgres.repositories;

import com.example.connectToPostgres.TestDataUtil;
import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;
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
public class AuthorRepositoryIntegrationsTests {

    private AuthorRepositories underTest;

    @Autowired
    public AuthorRepositoryIntegrationsTests(AuthorRepositories underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled(){

        AuthorEntity author = TestDataUtil.createTestAuthorA();
        underTest.save(author);
        Optional<AuthorEntity> result = underTest.findById(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);
        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
        underTest.save(authorB);
        AuthorEntity authorC = TestDataUtil.createTestAuthorC();
        underTest.save(authorC);

        Iterable<AuthorEntity> result = underTest.findAll();
        assertThat(result).hasSize(3);
        assertThat(result).contains(authorA, authorB, authorC);
    }

    @Test
    public void testThatAuthorCanBeUpdated (){
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = underTest.save(author);
        savedAuthor.setName("UPDATED NEW");
        underTest.save(savedAuthor);
        Optional<AuthorEntity> result = underTest.findById(savedAuthor.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedAuthor);
    }

    @Test
    public void testThatAuthorCanBeDeleted(){
        AuthorEntity author = TestDataUtil.createTestAuthorA();

        AuthorEntity savedAuthor = underTest.save(author);
        underTest.delete(savedAuthor);
        Optional<AuthorEntity> result = underTest.findById(savedAuthor.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetAuthorWithAgeLessThan() {
        AuthorEntity authorTestA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorA = underTest.save(authorTestA);

        AuthorEntity authorTestB = TestDataUtil.createTestAuthorB();
        AuthorEntity savedAuthorB = underTest.save(authorTestB);

        AuthorEntity authorTestC = TestDataUtil.createTestAuthorC();
        AuthorEntity savedAuthorC = underTest.save(authorTestC);

        Iterable<AuthorEntity> result = underTest.ageLessThan(50);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(savedAuthorB, savedAuthorC);
    }

    @Test
    public void testThatGetAuthorWithAgeGreaterThan() {
        AuthorEntity authorTestA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorA = underTest.save(authorTestA);

        AuthorEntity authorTestB = TestDataUtil.createTestAuthorB();
        AuthorEntity savedAuthorB = underTest.save(authorTestB);

        AuthorEntity authorTestC = TestDataUtil.createTestAuthorC();
        AuthorEntity savedAuthorC = underTest.save(authorTestC);

        Iterable<AuthorEntity> result = underTest.findAuthorsWithAgeGreaterThan(50);

        assertThat(result).containsExactly(savedAuthorA);
    }

}
