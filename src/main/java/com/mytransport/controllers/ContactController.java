package com.mytransport.controllers;

import com.mytransport.models.dto.ContactForm;
import com.mytransport.services.MailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String handleContactSubmit(
            @Valid @ModelAttribute ContactForm contactForm,
            BindingResult br,
            @RequestParam(value = "website", required = false) String website,
            @RequestParam(value = "source", required = false) String source,
            RedirectAttributes redirectAttributes) {

        boolean fromIndex = "index".equalsIgnoreCase(source);

        // honeypot → ако е попълнено, игнорирай като спам
        if (website != null && !website.isBlank()) {
            redirectAttributes.addFlashAttribute("success", "Запитването беше изпратено успешно!");
            return fromIndex ? "redirect:/#successMessage" : "redirect:/contact";
        }

        if (br.hasErrors()) {
            return fromIndex ? "index" : "contact";
        }

        String subject = "Ново запитване от EasyImport";
        String content = String.format(
                "Име: %s%n"+
                        "Емейл: %s%n"+
                        "От пристанище: %s%n" +
                        "До пристанище: %s%n"+
                        "Тип автомобил: %s%n"+
                        "Хибрид: %b%n"+
                        "Електрически: %b%n"+
                        "Година, марка и модел: %s%n",
                contactForm.getName(), contactForm.getEmail(),
                contactForm.getOriginPort(), contactForm.getDestinationPort(),
                contactForm.getVehicleType(), contactForm.isHybrid(), contactForm.isElectric(), contactForm.getVehicleDetails()
        );

        mailService.sendEmail(subject, content);

        redirectAttributes.addFlashAttribute("success", "Запитването беше изпратено успешно!");
        return fromIndex ? "redirect:/#successMessage" : "redirect:/contact#successMessage";
    }



}
