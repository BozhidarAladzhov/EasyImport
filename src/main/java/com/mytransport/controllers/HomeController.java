package com.mytransport.controllers;

import com.mytransport.models.OceanFreightEntity;
import com.mytransport.models.dto.TransportCalculationRequest;
import com.mytransport.repository.OceanFreightRepository;
import com.mytransport.services.TransportCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private TransportCalculatorService calculatorService;
    @Autowired
    private OceanFreightRepository oceanFreightRepository;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/process")
    public String process(){
        return "process";
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

        List<OceanFreightEntity> allPrices = oceanFreightRepository.findAll();

        // Списък с уникални 'originPort' стойности
        List<String> originPortList = allPrices.stream()
                .map(OceanFreightEntity::getOriginPort)
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
        List<OceanFreightEntity> pricesToVarna = allPrices.stream()
                .filter(p -> "Varna".equalsIgnoreCase(p.getDestinationPort()))
                .collect(Collectors.toList());

        List<OceanFreightEntity> pricesToRotterdam = allPrices.stream()
                .filter(p -> "Rotterdam".equalsIgnoreCase(p.getDestinationPort()))
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
        double oceanFreight = calculatorService.calculateOceanFreight(request);
        double terminalHandling = calculatorService.calculateTerminalHandling(request);
        double domesticRotterdam = calculatorService.calculateDomesticRotterdam(request);
        double vehicleTAX = calculatorService.calculateTAX(request);
        double calculateVATBulgaria = calculatorService.calculateVATBulgaria(request);
        double calculateVATNetherlands = calculatorService.calculateVATNetherlands(request);
        model.addAttribute("oceanFreight", oceanFreight);
        model.addAttribute("terminalHandling", terminalHandling);
        model.addAttribute("domesticRotterdam", domesticRotterdam);
        model.addAttribute("vehicleTAX", vehicleTAX);
        model.addAttribute("calculateVATBulgaria", calculateVATBulgaria);
        model.addAttribute("calculateVATNetherlands", calculateVATNetherlands);
        return "calculator";
    }

    /*@GetMapping("/contact")
    public String contact() {
        return "contact";
    }*/



}
