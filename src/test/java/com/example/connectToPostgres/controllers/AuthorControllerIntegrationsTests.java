package com.example.connectToPostgres.controllers;


import com.example.connectToPostgres.TestDataUtil;
import com.example.connectToPostgres.domain.dto.AuthorDto;
import com.example.connectToPostgres.domain.dto.entities.AuthorEntity;
import com.example.connectToPostgres.services.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest  // Startet die gesamte Spring Boot Applikation für einen Integrationstest
@ExtendWith(SpringExtension.class)  // Verbindet JUnit 5 mit Spring Framework für Dependency Injection
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  // Setzt den Application Context nach jedem Test komplett zurück für Isolation
@AutoConfigureMockMvc  // Konfiguriert MockMvc für das Testen von Web-Endpoints ohne echten HTTP-Server
public class AuthorControllerIntegrationsTests {
    private MockMvc mockMvc;

    private AuthorService authorService;

    private ObjectMapper objectMapper;

    @Autowired  // Weist Spring an, die MockMvc-Instanz automatisch zu injizieren, die durch @AutoConfigureMockMvc erstellt wurde
    public AuthorControllerIntegrationsTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Create() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuhtor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Jo meerkatz")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform( // Simuliert einen HTTP Request
                MockMvcRequestBuilders.get("/authors") // Erstellt einen GET-Request auf "/authors"
                        .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
        ).andExpect(MockMvcResultMatchers.status().isOk()); // erwarte, dass der HTTP Status 200 (OK) ist
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity authorEntity = authorService.save(TestDataUtil.createTestAuthorA());


        mockMvc.perform( // Simuliert einen HTTP Request
                MockMvcRequestBuilders.get("/authors") // Erstellt einen GET-Request auf "/authors"
                        .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Jo meerkatz")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(80)
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorService.save(authorEntity);

        mockMvc.perform( // Simuliert einen HTTP Request
                MockMvcRequestBuilders.get("/authors/1") // Erstellt einen GET-Request auf "/authors"
                        .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
        ).andExpect(MockMvcResultMatchers.status().isOk()); // erwarte, dass der HTTP Status 200 (OK) ist
    }

    @Test
    public void testThatReturnsHttpStatus404() throws Exception {

        mockMvc.perform( // Simuliert einen HTTP Request
                MockMvcRequestBuilders.get("/authors/99") // Erstellt einen GET-Request auf "/authors"
                        .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
        ).andExpect(MockMvcResultMatchers.status().isNotFound()); // erwarte, dass der HTTP Status 200 (OK) ist
    }

    @Test
    public void testThatGetAuthorReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorService.save(authorEntity);

        mockMvc.perform( // Simuliert einen HTTP Request
                        MockMvcRequestBuilders.get("/authors/1") // Erstellt einen GET-Request auf "/authors"
                                .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(1)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value("Jo meerkatz")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(80)
                );
    }

    @Test
    public void testThatUpdatesAuthorReturnsHttpStatus404WhenNotExists() throws Exception {
        AuthorDto authorDto = TestDataUtil.createTestAuthorADto();
        String authorDtoJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform( // Simuliert einen HTTP Request
                MockMvcRequestBuilders.get("/authors/99") // Erstellt einen GET-Request auf "/authors"
                        .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatUpdatesAuthorReturnsHttpStatus200WhenExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorADto();
        String authorDtoJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform( // Simuliert einen HTTP Request
                MockMvcRequestBuilders.get("/authors/" + savedAuthorEntity.getId()) // Erstellt einen GET-Request auf "/authors"
                        .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdatesAuthorReturnsAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity); // author a is in db

        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        authorEntityB.setId(savedAuthorEntity.getId()); // create new author b

        String authorB =  objectMapper.writeValueAsString(authorEntityB); // prepare author json for content


        mockMvc.perform( // Simuliert einen HTTP Request
                MockMvcRequestBuilders.put("/authors/" + authorEntityB.getId()) // Erstellt einen GET-Request auf "/authors"
                        .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
                        .content(authorB)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntityB.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntityB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntityB.getAge())
        );
    }

    @Test
    public void TestThatPartialUpdateAuthorReturnsHttpStatus200Ok() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity); // now, one record is in the db

        AuthorDto authorDto = TestDataUtil.createTestAuthorADto();
        authorDto.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform( // Simuliert einen HTTP Request
                MockMvcRequestBuilders.patch("/authors/" + savedAuthorEntity.getId()) // Erstellt einen GET-Request auf "/authors"
                        .contentType(MediaType.APPLICATION_JSON) // Sagt "Ich erwarte/sende JSON"
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestThatPartialUpdateAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity); // now, one record is in the db

        AuthorDto authorDto = TestDataUtil.createTestAuthorADto();
        authorDto.setName("UPDATED NAME"); // update 1

        String authorJSON = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED NAME")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(savedAuthorEntity.getAge())
        );
    }

    @Test
    public void TestThatDeleteAuthorReturnsHttpStatus404ForNonExistingId() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void TestThatDeleteAuthorReturnsHttpStatus402ForExistingId() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity); // now, one record is in the db

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
