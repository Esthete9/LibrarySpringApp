package com.example.SpringSecurity.finalSpringApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "book", schema = "public")
public class Book {
    @Id
    @Column(name = "id_book")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_book;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id_person")
    private Person owner;
    @Column(name = "name_of_book")
    @NotEmpty(message = "Название книги не может быть пустым")
    @Size(min = 1, max = 200, message = "Название книги должно содержать от 1 до 200 символов")
    private String nameOfBook;
    @Column(name = "date_of_writing")
    private int dateOfWriting;
    @Column(name = "author")
    @NotEmpty(message = "Имя автора не может быть пустым")
    @Size(min = 1, max = 100, message = "Имя автора должно содержать от 1 до 100 символов")
    private String author;
    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @Column(name = "img_url")
    private String imgUrl;
    @Transient
    private boolean expired;

    public Book() {}

    public Book(Date takenAt) {
        this.takenAt = takenAt;
    }

    public Book(int id_book, Integer id_person, String nameOfBook, String author, int dateOfWriting, Date takenAt) {
        this.id_book = id_book;
        this.nameOfBook = nameOfBook;
        this.author = author;
        this.dateOfWriting = dateOfWriting;
        this.takenAt = takenAt;
    }

    public Book(Person owner, String nameOfBook, String author) {
        this.owner = owner;
        this.nameOfBook = nameOfBook;
        this.author = author;
    }

    public Book(int dateOfWriting) {
        this.dateOfWriting = dateOfWriting;
    }

    public String getNameOfBook() {
        return nameOfBook;
    }

    public void setNameOfBook(String nameOfBook) {
        this.nameOfBook = nameOfBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDateOfWriting() {
        return dateOfWriting;
    }

    public void setDateOfWriting(int dateOfWriting) {
        this.dateOfWriting = dateOfWriting;
    }

    public int getId_book() {
        return id_book;
    }

    public void setId_book(int id_book) {
        this.id_book = id_book;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean getExpired() {
        return expired;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isHasOwner() {
        boolean bool = owner == null ? false : true;
        return bool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id_book != book.id_book) return false;
        if (dateOfWriting != book.dateOfWriting) return false;
        if (expired != book.expired) return false;
        if (!Objects.equals(owner, book.owner)) return false;
        if (!Objects.equals(nameOfBook, book.nameOfBook)) return false;
        if (!Objects.equals(author, book.author)) return false;
        if (!Objects.equals(takenAt, book.takenAt)) return false;
        return Objects.equals(imgUrl, book.imgUrl);
    }

    @Override
    public int hashCode() {
        int result = id_book;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (nameOfBook != null ? nameOfBook.hashCode() : 0);
        result = 31 * result + dateOfWriting;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (takenAt != null ? takenAt.hashCode() : 0);
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (expired ? 1 : 0);
        return result;
    }
}


