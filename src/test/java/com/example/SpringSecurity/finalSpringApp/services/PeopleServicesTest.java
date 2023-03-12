package com.example.SpringSecurity.finalSpringApp.services;

import com.example.SpringSecurity.finalSpringApp.model.Book;
import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.repositories.PeopleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PeopleServicesTest {

    @Autowired
    private PeopleServices peopleServices;

    @MockBean
    private PeopleRepository peopleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Person person;
    static Date date;
    static Book book;
    static List<Book> bookList;

    @BeforeAll
    public static void beforeAll() {
        date = new Date();
        book = new Book();
        bookList = new ArrayList<>();
    }

    @BeforeEach
    public void beforeEach() {
        person = new Person();
    }

    @Test
    public void save_ShouldSavePerson() {
        peopleServices.save(person);

        String expected = person.getRole();
        String actual = "ROLE_USER";

        assertNotNull(person.getRegistration_at());
        assertEquals(expected, actual);
    }

    @Test
    public void update_shouldUpdatePerson() {
        Person updatedPerson = new Person();
        updatedPerson.setPassword("123");
        updatedPerson.setRegistration_at(new Date());
        peopleServices.update(person, updatedPerson, 1);

        int actual = 1;
        int expected = person.getId_person();

        assertNotNull(person.getPassword());
        assertNotNull(person.getRegistration_at());
        assertEquals(expected, actual);
    }

    @Test
    public void getBooksWithRelations_overdueBooksShouldHaveExpiredTrue() {
        date.setDate(-100);
        book.setTakenAt(date);
        bookList.add(book);

        Person testPerson = new Person(bookList);
        Mockito.doReturn(Optional.of(testPerson)).when(peopleRepository).findById(1);

        boolean actual = true;
        boolean expected = peopleServices.getBooksWithRelations(1).get(0).getExpired();

        assertEquals(expected, actual);
    }

    @Test
    public void getBooksWithRelations_shouldReturnEmptyList() {
        Mockito.doReturn(Optional.ofNullable(null)).when(peopleRepository).findById(1);
        List<Book> actualList = Collections.EMPTY_LIST;
        List<Book> expectedList = peopleServices.getBooksWithRelations(1);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void activatePerson_shouldPersonSetRoleAndActivationCode() {
        person.setActivationCode("31231");
        Mockito.doReturn(person).when(peopleRepository).findByActivationCode("");

        Person actual = new Person();
        actual.setRole("ROLE_USER");

        peopleServices.activatePerson("");

        assertEquals(person, actual);
    }

    @Test
    public void activatePerson_shouldReturnFalse() {
        Mockito.doReturn(null).when(peopleRepository).findByActivationCode("");

        boolean actual = false;
        boolean expected = peopleServices.activatePerson("");

        assertEquals(expected, actual);
    }

    @Test
    public void cleanUsers_shouldDeleteUserIfDoesNotPassVerificationInHour() {
        date.setDate(-100);
        person.setRegistration_at(date);
        person.setRole("ROLE_NONVERIFIED");
        List<Person> people = new ArrayList<>(List.of(person));
        Optional<List<Person>> peopleByRole = Optional.of(people);
        Mockito.doReturn(peopleByRole).when(peopleRepository).findAllByRole("ROLE_NONVERIFIED");

        boolean actual = true;
        boolean expected = peopleServices.isCleanUsers();

        assertEquals(expected, actual);
    }

}