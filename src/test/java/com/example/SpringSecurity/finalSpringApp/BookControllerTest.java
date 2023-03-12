package com.example.SpringSecurity.finalSpringApp;

import com.example.SpringSecurity.finalSpringApp.controller.BookController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-book-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-book-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username = "zeavs9800@gmail.com", password = "admin", roles = "ADMIN")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookController bookController;


    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void beforeAll() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void bookMainPageTest() throws Exception {
        this.mockMvc.perform(get("/book"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void readAllBooks_shouldFindElementsWithGivenId() throws Exception {
        this.mockMvc.perform(get("/book"))
                .andDo(print())
                .andExpect(xpath("//*[@id='first-row']").nodeCount(3));
    }

    @Test
    public void createBook_shouldAddBookAndReturnStatus200() throws Exception {
        MockHttpServletRequestBuilder multipart = MockMvcRequestBuilders.multipart("/book")
                .file("file", "123".getBytes())
                .param("id_book", "4")
                .param("id_person", "4")
                .param("name_of_book", "test")
                .param("date_of_writing", "2000")
                .param("author", "testAuthor")
                .param("taken_at", "2023-02-03")
                .param("img_url", "/test")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void createBook_shouldFindAddedElement() throws Exception {
        this.mockMvc.perform(get("/book"))
                .andDo(print())
                .andExpect(xpath("//*[@id='first-row']").nodeCount(3));
    }

    @Test
    public void readOneBook_shouldOpenPage() throws Exception {
        this.mockMvc.perform(get("/book/1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateBookPatch_shouldUpdateBook() throws Exception {
        MockHttpServletRequestBuilder patch = MockMvcRequestBuilders.patch("/book/1")
                .param("name_of_book", "test")
                .param("date_of_writing", "2000")
                .param("author", "testAuthor")
                .param("taken_at", "2023-02-03")
                .param("img_url", "/test")
                .with(csrf());

        this.mockMvc.perform(patch)
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}