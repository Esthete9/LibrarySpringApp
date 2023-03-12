package com.example.SpringSecurity.finalSpringApp.repositories;

import com.example.SpringSecurity.finalSpringApp.model.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findByNameOfBookStartingWith(String startWith);
}
