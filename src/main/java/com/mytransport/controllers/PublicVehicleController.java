package com.mytransport.controllers;

import com.mytransport.models.Vehicle;
import com.mytransport.services.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PublicVehicleController {

    private final VehicleService vehicleService;

    public PublicVehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/v/{token}")
    public String viewByToken(@PathVariable String token, Model model) {
        return vehicleService.findByValidToken(token)
                .map(vehicle -> {
                    model.addAttribute("vehicle", vehicle);
                    model.addAttribute("photos", vehicleService.listPhotos(vehicle.getId()));
                    model.addAttribute("statusUpdates", vehicleService.listStatusUpdates(vehicle.getId()));
                    model.addAttribute("documents", vehicleService.listDocuments(vehicle.getId()));
                    return "public/vehicle-status";
                })
                .orElseGet(() -> {
                    Vehicle expired = vehicleService.findByToken(token).orElse(null);
                    model.addAttribute("expired", expired != null);
                    return "public/invalid-link";
                });
    }
}
