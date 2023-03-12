package com.example.SpringSecurity.finalSpringApp.controller;

import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.services.PeopleServices;
import com.example.SpringSecurity.finalSpringApp.util.PersonValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PeopleServices peopleServices;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PeopleServices peopleServices, PersonValidator personValidator) {
        this.peopleServices = peopleServices;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String readOfPeople(Model model) {
        model.addAttribute("allPerson", peopleServices.findAll());
        peopleServices.isCleanUsers();
        return "people/index";
    }

    @GetMapping("/{id}")
    public String readOfOnePeople(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleServices.findOne(id));
        model.addAttribute("books", peopleServices.getBooksWithRelations(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping
    public String createPeople(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleServices.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String updatePeopleGet(Model model, @PathVariable("id") int id, Authentication authentication) {
        Person person = peopleServices.findOne(id);
        model.addAttribute("person", person);
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePeoplePatch(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                                    @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().toString());
            return "people/edit";
        }
        peopleServices.update(person, peopleServices.findOne(id), id);

        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleServices.delete(id);
        return "redirect:/people";
    }

    @GetMapping("/search")
    public String searchPeople(Model model) {
        model.addAttribute("people", peopleServices.findByNameStartingWith("-"));
        return "people/search";
    }

    @PostMapping("/search")
    public String makeSearchPeople(@RequestParam(name = "startWith", required = false) String startWith, Model model) {
        if (startWith != null) {
            model.addAttribute("people", peopleServices.findByNameStartingWith(startWith));
        }
        return "people/search";
    }
}
