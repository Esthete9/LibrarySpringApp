package com.example.SpringSecurity.finalSpringApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "person", schema = "public")
public class Person {
    @Id
    @Column(name = "id_person")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_person;
    @Column(name = "name")
    @NotEmpty(message = "ФИО не может быть пустым")
    @Size(min = 3, max = 100, message = "ФИО должно содержать от 3 до 100 символов")
    @Pattern(regexp = "[a-яА-Я]+\\s{1,3}[a-яА-Я]+\\s{0,3}[a-яА-Я]*", message = "Должно быть указано полное имя человека, между словами не более 1 пробела")
    private String name;
    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Год рождения должен быть больше 1900")
    private int yearOfBirth;
    @Column(name = "username")
    @Email(message = "Incorrect email")
    @NotEmpty(message = "Email не может быть пустым")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "registration_at")
    private Date registration_at;

    @Column(name = "activation_code")
    private String activationCode;
    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<Book> bookList;

    public Person() {}

    public Person(int id_person, String name, int yearOfBirth, String username) {
        this.id_person = id_person;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.username = username;
    }

    public Person(List<Book> bookList) {
        this.bookList = bookList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }


    public int getId_person() {
        return id_person;
    }

    public void setId_person(int id_person) {
        this.id_person = id_person;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Date getRegistration_at() {
        return registration_at;
    }

    public void setRegistration_at(Date registration_at) {
        this.registration_at = registration_at;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id_person=" + id_person +
                ", name='" + name + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", activationCode='" + activationCode + '\'' +
                ", bookList=" + bookList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (id_person != person.id_person) return false;
        if (yearOfBirth != person.yearOfBirth) return false;
        if (!Objects.equals(name, person.name)) return false;
        if (!Objects.equals(username, person.username)) return false;
        if (!Objects.equals(password, person.password)) return false;
        if (!Objects.equals(role, person.role)) return false;
        if (!Objects.equals(activationCode, person.activationCode))
            return false;
        return Objects.equals(bookList, person.bookList);
    }

    @Override
    public int hashCode() {
        int result = id_person;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + yearOfBirth;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (activationCode != null ? activationCode.hashCode() : 0);
        result = 31 * result + (bookList != null ? bookList.hashCode() : 0);
        return result;
    }
}
