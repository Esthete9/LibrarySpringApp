package com.example.SpringSecurity.finalSpringApp.services;

import com.example.SpringSecurity.finalSpringApp.model.Person;
import com.example.SpringSecurity.finalSpringApp.repositories.PeopleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    private final MailSender mailSender;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Transactional
    public void registration(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_NONVERIFIED");
        person.setActivationCode(UUID.randomUUID().toString());
        person.setRegistration_at(new Date());
        peopleRepository.save(person);

        if (!StringUtils.isEmpty(person.getUsername())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Library. Please, visit next link: http://localhost:8080/auth/activate/%s",
                    person.getName(),
                    person.getActivationCode()
            );
            mailSender.send(person.getUsername(), "Activation code", message);
        }
    }

}
