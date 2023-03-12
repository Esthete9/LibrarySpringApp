package com.example.SpringSecurity.finalSpringApp.services;

import com.example.SpringSecurity.finalSpringApp.model.Book;
import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.repositories.PeopleRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleServices {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passEncoder;
    private final BooksServices booksServices;

    @Autowired
    public PeopleServices(PeopleRepository peopleRepository,PasswordEncoder passEncoder, BooksServices booksServices) {
        this.peopleRepository = peopleRepository;
        this.passEncoder = passEncoder;
        this.booksServices = booksServices;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Optional<Person> findByEmail(String email) {
        return peopleRepository.findByUsername(email);
    }

    @Transactional
    public void save(Person person) {
        person.setRole("ROLE_USER");
        person.setPassword(passEncoder.encode(person.getPassword()));
        person.setRegistration_at(new Date());
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person updatePerson, Person person, int id) {
        updatePerson.setPassword(person.getPassword());
        updatePerson.setId_person(id);
        updatePerson.setRegistration_at(person.getRegistration_at());
        peopleRepository.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooksWithRelations(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(findOne(id).getBookList());
            final long tenDaysInMillisec = 864_000_000L;
            person.get().getBookList().forEach(book -> {
                if (new Date().getTime() - book.getTakenAt().getTime() >= tenDaysInMillisec) {
                    book.setExpired(true);
                }
            });
            return person.get().getBookList();
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Transactional
    public boolean activatePerson(String code) {
        Person person = peopleRepository.findByActivationCode(code);
        if (person == null) {
            return false;
        }
        person.setActivationCode(null);
        person.setRole("ROLE_USER");
        peopleRepository.save(person);
        return true;
    }

    public String splitName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            return "";
        }
       String[] strings = name.split(" ");
       String res = strings.length > 2 ? strings[0] + " " + strings[1] : strings[0];
       return res;
    }

    public List<Person> findByNameStartingWith(String startWith) {
        List<Person> people = peopleRepository.findByNameStartingWith(booksServices.firstCharToUpperCase(startWith));
        if (people.isEmpty()) {
            people = Collections.EMPTY_LIST;
        }
        return people;
    }

    @Transactional
    public boolean isCleanUsers() {
        boolean res = false;
        if (peopleRepository.findAllByRole("ROLE_NONVERIFIED").isPresent()) {
            long oneHourInMillisec = 3_600_000L;
            for (var person : peopleRepository.findAllByRole("ROLE_NONVERIFIED").get()) {
                System.out.println("i work 2");
                if (new Date().getTime() - person.getRegistration_at().getTime() >= oneHourInMillisec
                        && "ROLE_NONVERIFIED".equals(person.getRole())) {
                    peopleRepository.delete(person);
                    res = true;
                }
            }
        }
        return res;
    }


}
