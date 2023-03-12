package com.example.SpringSecurity.finalSpringApp.controller;

import com.example.SpringSecurity.finalSpringApp.model.Book;
import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.security.PersonDetails;
import com.example.SpringSecurity.finalSpringApp.services.BooksServices;
import com.example.SpringSecurity.finalSpringApp.services.PeopleServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/book")
public class BookController {

    @Value("${upload.path}")
    private String uploadPath;
    private final BooksServices booksServices;
    private final PeopleServices peopleServices;

    @Autowired
    public BookController(BooksServices booksServices, PeopleServices peopleServices) {
        this.booksServices = booksServices;
        this.peopleServices = peopleServices;
    }

    @GetMapping
    public String readAllBooks(Model model, @RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "books_per_page", required = false) Integer books_per_page,
                               @RequestParam(value = "sort_by_year", required = false) Boolean sort_by_year,
                               @RequestParam(value = "sort_by_year_Desc", required = false) Boolean sort_by_year_Desc,
                               Authentication authentication) {

        if (authentication == null)
            model.addAttribute("authenticated", false);
        else
            model.addAttribute("authenticated", true);


        if (page == null && books_per_page == null) {
            page = 0;
            books_per_page = 12;
        }

        if (authentication != null) {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            model.addAttribute("personId", personDetails.getPerson().getId_person());
            model.addAttribute("personName", peopleServices.splitName(personDetails.getPerson().getName()));
        }

        List<List<Book>> booksRow = booksServices.pushBookOnView(page, books_per_page, sort_by_year, sort_by_year_Desc);
        model.addAttribute("firstRow", booksRow.get(0));
        model.addAttribute("secondRow", booksRow.get(1));
        model.addAttribute("thirdRow", booksRow.get(2));

        model.addAttribute("pages", booksServices.getListOfPageIndexes());
        model.addAttribute("currentPage", page);

        model.addAttribute("sort_by_yea", sort_by_year);
        model.addAttribute("sort_by_year_Des", sort_by_year_Desc);

        return "book/index";
    }

    @GetMapping("/{id}")
    public String readOneBook(Model model, @PathVariable("id") int id, @ModelAttribute("person") Person person,
                              Authentication authentication) {
        model.addAttribute("book", booksServices.findOne(id));

        Optional<Person> owner = booksServices.findOwner(id);
        if (owner.isPresent())  {
            model.addAttribute("owner", owner.get());
        } else if (!owner.isPresent()) {
            model.addAttribute("people", peopleServices.findAll());
        }
        return "book/show";
    }

    @PatchMapping("/{id}/choosePerson")
    public String choosePerson(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        booksServices.updateBookPersonId(person, id);
        return "redirect:/book/{id}";
    }

    @PatchMapping("/{id}/returnBook")
    public String returnBook(@PathVariable("id") int id) {
        booksServices.returnBook(id);
        return "redirect:/book/{id}";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "book/new";
    }

    @PostMapping
    public String createBook(@RequestParam(name = "file", required = false) MultipartFile file, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return "book/new";
        }

        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String imgURL = "/images/" + uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + uuidFile + "." + file.getOriginalFilename()));

            book.setImgUrl(imgURL);
        }

        booksServices.create(book);

        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String updateBookGet(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksServices.findOne(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String updateBookPatch(@RequestParam(name = "file", required = false) MultipartFile file, @ModelAttribute("book") @Valid Book updateBook, BindingResult bindingResult, @PathVariable("id") int id) throws IOException {

        if (bindingResult.hasErrors()) {
            return "book/edit";
        }

        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String imgURL = "/images/" + uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File("C:" + uploadPath + "/" + uuidFile + "." + file.getOriginalFilename()));

            updateBook.setImgUrl(imgURL);
        }

        Book book = booksServices.findOne(id);
        booksServices.update(book, updateBook, id);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksServices.delete(id);
        return "redirect:/book";
    }

    @GetMapping("/search")
    public String searchBook(Model model) {
        model.addAttribute("books", booksServices.findByNameOfBookStartingWith("-"));
        return "book/search";
    }

    @PostMapping("/search")
    public String makeSearchBook(@RequestParam(name = "startWith", required = false) String startWith, Model model) {
        if (startWith != null) {
            model.addAttribute("books", booksServices.findByNameOfBookStartingWith(startWith));
        }
        return "book/search";
    }

}
