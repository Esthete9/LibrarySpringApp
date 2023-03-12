package com.example.SpringSecurity.finalSpringApp.util;

import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.services.PeopleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleServices peopleServices;

    @Autowired
    public PersonValidator( PeopleServices peopleServices) {
        this.peopleServices = peopleServices;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        if (peopleServices.findByEmail(person.getUsername()).isPresent()) {
             errors.rejectValue("username", "", "This email is already taken");
        }
    }
}
