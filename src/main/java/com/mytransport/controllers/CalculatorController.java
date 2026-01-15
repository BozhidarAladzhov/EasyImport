package com.mytransport.controllers;

import com.mytransport.models.dto.TransportCalculationRequest;
import com.mytransport.services.TransportCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CalculatorController {

    @Autowired
    private TransportCalculatorService calculatorService;

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

}


