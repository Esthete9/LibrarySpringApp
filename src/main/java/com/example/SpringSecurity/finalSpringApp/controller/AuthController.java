package com.example.SpringSecurity.finalSpringApp.controller;

import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.services.PeopleServices;
import com.example.SpringSecurity.finalSpringApp.services.RegistrationService;
import com.example.SpringSecurity.finalSpringApp.util.PersonValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;

    private final PeopleServices peopleServices;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, PeopleServices peopleServices) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.peopleServices = peopleServices;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("person", new Person());

        return "auth/regestration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/regestration";
        }

        registrationService.registration(person);
        return "/auth/emailVerification";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = peopleServices.activatePerson(code);

        if (isActivated) {
            model.addAttribute("message", "Вы успешно зарегестрированы введите email и пароль для входа");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "/auth/login";
    }
}
