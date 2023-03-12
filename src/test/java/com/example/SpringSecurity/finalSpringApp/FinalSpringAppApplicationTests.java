package com.example.SpringSecurity.finalSpringApp;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.SpringSecurity.finalSpringApp.controller.BookController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FinalSpringAppApplicationTests {

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
	void contextLoads() throws Exception {
		assertThat(bookController).isNotNull();
	}

	@Test
	public void loginTest() throws Exception {
		this.mockMvc.perform(get("/people"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/auth/login"));
	}

	@Test
	public void badCredentials() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").param("user", "Alfred"))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "zeavs9800@gmail.com", password = "admin", roles = "USER")
	public void testAuth() throws Exception {
		mockMvc.perform(get("/people"))
				.andExpect(status().is4xxClientError());
	}

}


