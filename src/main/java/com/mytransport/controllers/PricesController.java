package com.mytransport.controllers;

import com.mytransport.models.entity.OceanFreightEntity;
import com.mytransport.repository.OceanFreightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PricesController {

    @Autowired
    private OceanFreightRepository oceanFreightRepository;

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
                .filter(p -> "VARNA".equalsIgnoreCase(p.getDestinationPort()))
                .collect(Collectors.toList());

        List<OceanFreightEntity> pricesToRotterdam = allPrices.stream()
                .filter(p -> "ROTTERDAM".equalsIgnoreCase(p.getDestinationPort()))
                .collect(Collectors.toList());

        model.addAttribute("pricesToVarna", pricesToVarna);
        model.addAttribute("pricesToRotterdam", pricesToRotterdam);
        model.addAttribute("selectedPort", port);
        model.addAttribute("selectedOriginPort", originPort);
        model.addAttribute("originPortList", originPortList);

        return "prices";
    }

}
