package com.example.SpringSecurity.finalSpringApp.services;

import com.example.SpringSecurity.finalSpringApp.model.Book;
import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.repositories.BooksRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class BooksServices {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksServices(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void create(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(Book book, Book updateBook, int id) {
        updateBook.setId_book(id);
        updateBook.setOwner(book.getOwner());
        updateBook.setTakenAt(book.getTakenAt());
        booksRepository.save(updateBook);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void updateBookPersonId(Person person, int id) {
        Book book = findOne(id);
        book.setOwner(person);
        book.setTakenAt(new Date());
    }

    @Transactional
    public void returnBook(int id) {
        Book book = booksRepository.getOne(id);
        book.setTakenAt(null);
        book.setOwner(null);
    }

    public Optional<Person> findOwner(int id) {
        Optional<Person> optionalOwner = Optional.ofNullable(findOne(id).getOwner());
        return optionalOwner;
    }

    public List<List<Book>> splitBooks(List<Book> bookList, int booksPerInternalList, int countElementInExternalList) {
        List<List<Book>> listList = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < countElementInExternalList; i++) {
            listList.add(new ArrayList<>());
            while (listList.get(i).size() < booksPerInternalList && index != bookList.size()) {
                listList.get(i).add(bookList.get(index));
                index++;
            }
        }
        return listList;
    }

    public List<Book> booksPaginationAndSort(Integer page, Integer booksPerPage, Boolean sortASC, Boolean sortDESC) {
        if (sortASC != null && sortASC) {
            return splitBooks(sortBooks(SortRule.ASC), booksPerPage, getCountPages()).get(page);
        } else if (sortDESC != null && sortDESC) {
            return splitBooks(sortBooks(SortRule.DESC), booksPerPage, getCountPages()).get(page);
        } else if (page != null && booksPerPage != null) {
            return splitBooks(booksRepository.findAll(), booksPerPage, getCountPages()).get(page);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public int getCountPages() {
        List<Book> bookList = booksRepository.findAll();
        int contPages = bookList.size() % 12 == 0 ? bookList.size() / 12 : bookList.size() / 12 + 1;
        return contPages;
    }

    public List<Integer> getListOfPageIndexes() {
        List<Integer> pageIndexesList = new ArrayList<>();
        int countPages = getCountPages();
        for (int i = 0; i < countPages; i++) {
            pageIndexesList.add(i);
        }
        return pageIndexesList;
    }

    public List<List<Book>> pushBookOnView(Integer page, Integer booksPerPage, Boolean sortASC, Boolean sortDesc) {
        if (page != null && booksPerPage != null)
           return splitBooks(booksPaginationAndSort(page, booksPerPage, sortASC, sortDesc),4, 3);
        else return Collections.EMPTY_LIST;
    }

    public List<Book> sortBooks(SortRule rule) {
        List<Book> newBookList = new ArrayList<>(booksRepository.findAll());
        if (rule == SortRule.ASC) {
            Collections.sort(newBookList, (o1, o2) -> {
                return sortRule(o1, o2, SortRule.ASC);
            });
        } else if (rule == SortRule.DESC) {
            Collections.sort(newBookList, (o1, o2) -> {
                return sortRule(o1, o2, SortRule.DESC);
            });
        }
        return newBookList;
    }

    private int sortRule(Book first, Book second, SortRule rule) {
        int res = 0;
        switch (rule) {
            case ASC:
                if (first.getDateOfWriting() > second.getDateOfWriting()) {
                    res = 1;
                } else if (first.getDateOfWriting() < second.getDateOfWriting()) {
                    res = -1;
                } else {
                    res = 0;
                }
                break;
            case DESC:
                if (first.getDateOfWriting() > second.getDateOfWriting()) {
                    res = -1;
                } else if (first.getDateOfWriting() < second.getDateOfWriting()) {
                    res = 1;
                } else {
                    res = 0;
                }
                break;
        }
        return res;
    }

    public List<Book> findByNameOfBookStartingWith(String startWith) {
        List<Book> books = booksRepository.findByNameOfBookStartingWith(firstCharToUpperCase(startWith));
        if (books.isEmpty()) {
            books = Collections.EMPTY_LIST;
        }
        return books;
    }

    public String firstCharToUpperCase(String startWith) {
        if (startWith.isEmpty() || startWith.isBlank()) {
            return "";
        }

        char[] str = startWith.toCharArray();
        char[] chars = {str[0]};
        String toUpp = String.valueOf(chars).toUpperCase();
        str[0] = toUpp.toCharArray()[0];
        return String.valueOf(str);
    }

}
