package com.example.SpringSecurity.finalSpringApp.services;

import com.example.SpringSecurity.finalSpringApp.model.Book;
import com.example.SpringSecurity.finalSpringApp.repositories.BooksRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BooksServicesTest {

    @MockBean
    private BooksRepository booksRepository;

    @Autowired
    private BooksServices booksServices;

    private List<Book> bookList;

    private List<Book> listToSort;

    @BeforeEach
    public void setUp() {
        bookList = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            bookList.add(new Book());
        }

        for (int i = 0; i < bookList.size(); i++) {
            bookList.get(i).setDateOfWriting(i);
        }

        listToSort = new ArrayList<>(List.of(
                new Book(2), new Book(3), new Book(1),
                new Book(12), new Book(11), new Book(4),
                new Book(5), new Book(6), new Book(8),
                new Book(7), new Book(9), new Book(10),
                new Book(0)
        ));
    }

    @Test
    public void splitBooks_shouldListOfListsDependingVariables() {
        List<List<Book>> actual = new ArrayList<>(List.of
                (new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        actual.get(0).add(new Book());
        actual.get(1).add(new Book());
        actual.get(2).add(new Book());

        List<Book> initialList = new ArrayList<>(List.of(new Book(), new Book(), new Book()));
        List<List<Book>> expected = booksServices.splitBooks(initialList, 1, 3);

        assertEquals(expected, actual);
    }

    @Test
    public void splitBooks_shouldNotReturnNull() {
        List<Book> initialList = new ArrayList<>();
        List<List<Book>> expected = booksServices.splitBooks(initialList, 2, 1);
        assertNotNull(expected);
    }

    @Test
    public void getCountPages_shouldReturnCountPages() {
        Mockito.doReturn(bookList).when(booksRepository).findAll();

        int actual = 2;;
        int expected = booksServices.getCountPages();

        assertEquals(expected, actual);
    }

    @Test
    public void getListOfPageIndexes_shouldReturnListContainingIndexesOfAllPages() {
        Mockito.doReturn(bookList).when(booksRepository).findAll();

        List<Integer> actualBookList = new ArrayList<>(List.of(0, 1));
        List<Integer> expectedBookList = booksServices.getListOfPageIndexes();

        assertEquals(expectedBookList, actualBookList);
    }

    @Test
    public void getListOfPageIndexes_shouldReturnNotNull() {
        List<Integer> expectedBookList = booksServices.getListOfPageIndexes();

        assertNotNull(expectedBookList);
    }

    @Test
    public void sortBooks_shouldReturnSortedListASC() {
        List<Book> actualBookList = new ArrayList<>(bookList);

        Mockito.doReturn(listToSort).when(booksRepository).findAll();
        List<Book> expectedList = booksServices.sortBooks(SortRule.ASC);

        assertEquals(expectedList, actualBookList);
    }

    @Test
    public void sortBooks_shouldReturnSortedListDESC() {
        int val = 12;
        for (int i = 0; i < bookList.size(); i++) {
            bookList.get(i).setDateOfWriting(val);
            val--;
        }
        
        List<Book> actualBookList = new ArrayList<>(bookList);

        Mockito.doReturn(listToSort).when(booksRepository).findAll();
        List<Book> expectedList = booksServices.sortBooks(SortRule.DESC);

        assertEquals(expectedList, actualBookList);
    }

    @Test
    public void firstCharToUpperCase_shouldTransformFirstCharAtStringToUppercase() {
        String actual = "Hello";
        String expected = booksServices.firstCharToUpperCase("hello");

        assertEquals(expected, actual);
    }

    @Test
    public void booksPaginationAndSort_shouldReturnPartOfListSortedByASC() {
        Mockito.doReturn(listToSort).when(booksRepository).findAll();

        List<Book> actual = new ArrayList<>();
        actual.add(bookList.get(bookList.size() - 1));
        List<Book> expected = booksServices.booksPaginationAndSort(1, 12, true, false);

        assertEquals(expected, actual);
    }

    @Test
    public void booksPaginationAndSort_shouldReturnPartOfListSortedByDESC() {
        Mockito.doReturn(listToSort).when(booksRepository).findAll();

        List<Book> actual = new ArrayList<>();
        actual.add(bookList.get(0));
        List<Book> expected = booksServices.booksPaginationAndSort(1, 12, false, true);

        assertEquals(expected, actual);
    }

    @Test
    public void booksPaginationAndSort_shouldReturnPartOfListNotSorted() {
        Mockito.doReturn(listToSort).when(booksRepository).findAll();

        List<Book> actual = new ArrayList<>();
        actual.add(listToSort.get(listToSort.size() - 1));
        List<Book> expected = booksServices.booksPaginationAndSort(1, 12, false, false);

        assertEquals(expected, actual);
    }

    @Test
    public void booksPaginationAndSort_shouldReturnReturnNotNull() {
        Mockito.doReturn(listToSort).when(booksRepository).findAll();
        List<Book> expected = booksServices.booksPaginationAndSort(null, null, false, false);

        assertNotNull(expected);
    }

    @Test
    public void pushBookOnView_shouldReturnFirstElementListOfListsOnSecondPage() {
        Mockito.doReturn(listToSort).when(booksRepository).findAll();

        List<List<Book>> actual = new ArrayList<>(List.of(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        actual.get(0).add(bookList.get(bookList.size() - 1));
        List<List<Book>> expected = booksServices.pushBookOnView(1, 12, true, false);

        assertEquals(expected, actual);
    }

    @Test
    public void pushBookOnView_shouldReturnNotNull() {
        Mockito.doReturn(listToSort).when(booksRepository).findAll();

        List<List<Book>> expected = booksServices.pushBookOnView(null, null, null, null);

        assertNotNull(expected);
    }
}
