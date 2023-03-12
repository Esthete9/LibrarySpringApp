package com.example.SpringSecurity.finalSpringApp.services;

import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.repositories.PeopleRepository;
import org.checkerframework.checker.fenum.qual.AwtAlphaCompositingRule;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationServiceTest {

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    PeopleRepository peopleRepository;

    @MockBean
    MailSender mailSender;

    @Autowired
    RegistrationService registrationService;

    @Test
    public void registration_() {
        Person person = new Person();
        person.setPassword("213");
        person.setUsername("st@mail.ru");
        registrationService.registration(person);

        Mockito.verify(passwordEncoder).encode("213");
        Mockito.verify(peopleRepository).save(person);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(ArgumentMatchers.eq(person.getUsername()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());


        String actual = "ROLE_NONVERIFIED";
        String expected = person.getRole();

        assertEquals(expected, actual);
    }
}