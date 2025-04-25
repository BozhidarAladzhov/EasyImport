package com.mytransport.controllers;

import com.mytransport.models.TransportPrice;
import com.mytransport.models.TransportPriceEntity;
import com.mytransport.models.dto.TransportCalculationRequest;
import com.mytransport.repository.TransportPriceRepository;
import com.mytransport.services.TransportCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public String prices(
            @RequestParam(required = false) String port,
            @RequestParam(required = false) String originPort,
            Model model) {

        List<TransportPriceEntity> allPrices = transportPriceRepository.findAll();

        // Списък с уникални 'originPort' стойности
        List<String> originPortList = allPrices.stream()
                .map(TransportPriceEntity::getOriginPort)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Филтриране по дестинация
        if (port != null && !port.isBlank()) {
            allPrices = allPrices.stream()
                    .filter(p -> port.equalsIgnoreCase(p.getDestinationPort()))
                    .collect(Collectors.toList());
        }

        // Филтриране по 'originPort'
        if (originPort != null && !originPort.isBlank()) {
            allPrices = allPrices.stream()
                    .filter(p -> originPort.equalsIgnoreCase(p.getOriginPort()))
                    .collect(Collectors.toList());
        }


        // Разделяне по дестинация
        List<TransportPriceEntity> pricesToVarna = allPrices.stream()
                .filter(p -> "VARNA".equalsIgnoreCase(p.getDestinationPort()))
                .collect(Collectors.toList());

        List<TransportPriceEntity> pricesToRotterdam = allPrices.stream()
                .filter(p -> "ROTTERDAM".equalsIgnoreCase(p.getDestinationPort()))
                .collect(Collectors.toList());

        model.addAttribute("pricesToVarna", pricesToVarna);
        model.addAttribute("pricesToRotterdam", pricesToRotterdam);
        model.addAttribute("selectedPort", port);
        model.addAttribute("selectedOriginPort", originPort);
        model.addAttribute("originPortList", originPortList);

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
