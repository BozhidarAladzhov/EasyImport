package com.mytransport.controllers;

import com.mytransport.models.dto.ContactForm;
import com.mytransport.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    private final MailService mailService;

    @Autowired
    public ContactController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "contact";
    }

    @PostMapping("/contact")
    public String handleContactSubmit(@ModelAttribute ContactForm contactForm, RedirectAttributes redirectAttributes) {

        String subject = "Ново запитване от EasyImport";
        String content = String.format("Име: %s\nЕмейл: %s\nОт: %s\nДо: %s\nТип автомобил: %s\nХибрид: %b\nЕлектрически: %b",
                contactForm.getName(),
                contactForm.getEmail(),
                contactForm.getOriginPort(),
                contactForm.getDestinationPort(),
                contactForm.getVehicleType(),
                contactForm.isHybrid(),
                contactForm.isElectric()
        );

        mailService.sendEmail(subject, content);

        redirectAttributes.addFlashAttribute("success", "Запитването беше изпратено успешно!");
        return "redirect:/contact";
    }


}
