package com.bezkoder.spring.login.controllers;


import com.bezkoder.spring.login.models.ContactForm;
import com.bezkoder.spring.login.repository.ContactFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ContactController {
    @Autowired
    private ContactFormRepository contactFormRepository;



    @PostMapping("/contact")
    public String saveContact(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("subject") String subject, @RequestParam("message") String message) {

        ContactForm contactForm = new ContactForm();

        contactForm.setName(name);
        contactForm.setEmail(email);
        contactForm.setSubject(subject);
        contactForm.setMessage(message);
        contactFormRepository.save(contactForm);
        return " message envoye";
    }


}
