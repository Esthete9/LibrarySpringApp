package com.example.SpringSecurity.finalSpringApp.services;

import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.repositories.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.reflect.Executable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonDetailsServiceTest {

    @MockBean
    PeopleRepository peopleRepository;

    @Autowired
    PersonDetailsService personDetailsService;

    @Test()
    public void loadUserByUsername_shouldThrowException() {
        Mockito.doReturn(Optional.ofNullable(null)).when(peopleRepository).findByUsername("");
        assertThrows(UsernameNotFoundException.class, () -> {personDetailsService.loadUserByUsername("");});
    }
}