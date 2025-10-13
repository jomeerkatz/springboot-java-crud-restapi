package com.example.connectToPostgres.controllers;

import com.example.connectToPostgres.TestDataUtil;
import com.example.connectToPostgres.domain.dto.BookDto;
import com.example.connectToPostgres.domain.dto.entities.BookEntity;
import com.example.connectToPostgres.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BooksControllerIntegrationsTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookService bookService;

    @Autowired
    public BooksControllerIntegrationsTests(MockMvc mockMvc, BookService bookService){
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.bookService = bookService;
    }

    @Test
    public void TestThatCreateBookReturnsHttpStatus201Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBookJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void TestThatCreateBookReturnsCreatedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );
    }

    @Test
    public void TestThatListBookReturnsHttpStatus200Ok() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void TestThatListBooksReturnsListOfAuthors() throws Exception {
       BookEntity bookEntity = TestDataUtil.createTestBookA(null);
       BookEntity savedBookEntity = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

       mockMvc.perform(
               MockMvcRequestBuilders.get("/books")
                       .contentType(MediaType.APPLICATION_JSON)
       ).andExpect(
               MockMvcResultMatchers.jsonPath("$[0].isbn").value("isbnTestA")
       ).andExpect(
               MockMvcResultMatchers.jsonPath("$[0].title").value("tempA")
       );

    }

    @Test
    public void TestThatGetBookReturnsHttpStatus200WhenBookExists() throws Exception{
        // create a book
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        // save the book
        bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);
        // simulate a request to h2 database to get the book
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()); // check, if https status is 200 - it exists
    }

    @Test
    public void TestThatTriesToGetBookButNotFoundCheckHttpStatus404() throws Exception{
        // create a book
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        // simulate a request to h2 database to get the book
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()); // check, if https status is 200 - it exists
    }

    @Test
    public void testThatGetBookReturnsBookWhenBookExists() throws Exception{
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value("isbnTestA")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value("tempA")
                );
    }


    @Test
    public void testThatUpdateBookReturnsHttpStatus200Created() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        // perform update
        bookDto.setIsbn(bookDto.getIsbn());
        String bookJSON = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }

    @Test
    public void TestThatUpdateBookReturnsUpdatedBook() throws Exception {
//        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
//        String createBookJson = objectMapper.writeValueAsString(bookDto);

        BookEntity newBook = TestDataUtil.createTestBookA(null);
        BookEntity savedBookEntity = bookService.createUpdateBook(newBook.getIsbn(), newBook);

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookEntity.setIsbn(newBook.getIsbn());
        bookEntity.setTitle("UPDATED TEST HERE");
        String bookJSON = objectMapper.writeValueAsString(bookEntity);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + savedBookEntity.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED TEST HERE")
        );
    }

    @Test
    public void TestThatPartialUpdatesBooksReturnsHttpStatus200() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookB(null);
        BookEntity savedBookEntity = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setTitle("NEW PARTIAL UPDATE HERE");

        String bookJSON = objectMapper.writeValueAsString(bookDto);

        // create request
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestThatPartialUpdatesBooksReturnsUpdatedBook() throws Exception {
        // create db record so we can update SOMETHING
        BookEntity bookEntity = TestDataUtil.createTestBookB(null);
        BookEntity savedBookEntity = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity); // push into db

        // create the record with new title for example...
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setTitle("NEW PARTIAL UPDATE HERE");

        // create the JSON for the request
        String bookJSON = objectMapper.writeValueAsString(bookDto);

        // create request
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("NEW PARTIAL UPDATE HERE")
        );
    }
}

