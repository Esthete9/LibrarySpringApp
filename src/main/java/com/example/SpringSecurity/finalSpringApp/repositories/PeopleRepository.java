package com.example.SpringSecurity.finalSpringApp.repositories;

import com.example.SpringSecurity.finalSpringApp.model.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    //List<Person> findByEmail(String email);
    Optional<Person> findByUsername(String username);

    Person findByActivationCode(String code);

    List<Person> findByNameStartingWith(String startWith);

    Optional<List<Person>> findAllByRole(String role);
}
