package com.bezkoder.spring.login.controllers;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bezkoder.spring.login.models.ContactForm;
import com.bezkoder.spring.login.repository.ContactFormRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;
@RestController
@RequestMapping("/api/auth")
public class ContactController {

    @Autowired
    private ContactFormRepository contactFormRepository;

    @PostMapping("/contact")
    public String submitContactForm(@ModelAttribute("contactForm") ContactForm contactForm, Model model) {
        // Enregistrement des données dans la base de données
        contactFormRepository.save(contactForm);

        // Envoi de l'e-mail à l'administrateur du site
        String recipientAddress = "admin@example.com";
        String subject = "Nouveau formulaire de contact reçu";
        String message = "Nom: " + contactForm.getName() + "\n"
                + "E-mail: " + contactForm.getEmail() + "\n"
                + "Sujet: " + contactForm.getSubject() + "\n"
                + "Message: " + contactForm.getMessage();
        sendEmail(recipientAddress, subject, message);

        // Ajout d'un message de confirmation dans le modèle
        model.addAttribute("message", "Le formulaire de contact a été envoyé avec succès !");

        // Redirection vers la page de confirmation
        return "confirmation";
    }

    private void sendEmail(String recipientAddress, String subject, String message) {
        // Configuration de la session JavaMail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your-email@gmail.com", "your-email-password");
            }
        });

        try {
            // Création du message
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress("your-email@gmail.com"));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));
            emailMessage.setSubject(subject);
            emailMessage.setText(message);

            // Envoi du message
            Transport.send(emailMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
