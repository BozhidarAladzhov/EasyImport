package com.mytransport.controllers;

import com.mytransport.models.TransportPrice;
import com.mytransport.models.dto.TransportCalculationRequest;
import com.mytransport.repository.TransportPriceRepository;
import com.mytransport.services.TransportCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TransportCalculatorService calculatorService;
    @Autowired
    private TransportPriceRepository transportPriceRepository;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/services")
    public String services(){
        return "services";
    }

    @GetMapping("/prices")
    public String prices(Model model) {
        model.addAttribute("prices", transportPriceRepository.findAll());
        return "prices";
    }

    @GetMapping("/calculator")
    public String calculatorForm(Model model) {
        model.addAttribute("request", new TransportCalculationRequest());
        return "calculator";
    }

    @PostMapping("/calculator")
    public String calculate(@ModelAttribute("request") TransportCalculationRequest request, Model model) {
        double result = calculatorService.calculate(request);
        model.addAttribute("result", result);
        return "calculator";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }



}
