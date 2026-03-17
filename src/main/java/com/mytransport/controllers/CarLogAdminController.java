package com.mytransport.controllers;

import com.mytransport.models.carlog.Car;
import com.mytransport.models.carlog.CarAccountRole;
import com.mytransport.models.carlog.FuelType;
import com.mytransport.models.carlog.PaymentPlan;
import com.mytransport.models.carlog.CarUser;
import com.mytransport.models.dto.carlog.CarForm;
import com.mytransport.models.dto.carlog.CarUserForm;
import com.mytransport.services.CarLogService;
import com.mytransport.services.CarUserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carlog/admin")
public class CarLogAdminController {

    private final CarLogService carLogService;
    private final CarUserService carUserService;

    public CarLogAdminController(CarLogService carLogService, CarUserService carUserService) {
        this.carLogService = carLogService;
        this.carUserService = carUserService;
    }

    @GetMapping("/cars/new")
    public String createCarForm(Model model) {
        model.addAttribute("carForm", new CarForm());
        addCarMeta(model);
        return "carlog/car-form";
    }

    @PostMapping("/cars")
    public String createCar(@Valid @ModelAttribute("carForm") CarForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addCarMeta(model);
            return "carlog/car-form";
        }
        Car car = carLogService.createCar(form);
        redirectAttributes.addFlashAttribute("success", "Автомобилът е създаден.");
        return "redirect:/carlog/cars/" + car.getId();
    }

    @GetMapping("/cars/{id}/edit")
    public String editCarForm(@PathVariable Long id, Model model) {
        Car car = carLogService.findById(id).orElseThrow();
        model.addAttribute("car", car);
        model.addAttribute("carForm", mapCar(car));
        addCarMeta(model);
        return "carlog/car-form";
    }

    @PostMapping("/cars/{id}")
    public String updateCar(@PathVariable Long id,
                            @Valid @ModelAttribute("carForm") CarForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("car", carLogService.findById(id).orElseThrow());
            addCarMeta(model);
            return "carlog/car-form";
        }
        carLogService.updateCar(id, form);
        redirectAttributes.addFlashAttribute("success", "Автомобилът е обновен.");
        return "redirect:/carlog/cars/" + id;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", carUserService.findAll());
        return "carlog/users";
    }

    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        model.addAttribute("userForm", new CarUserForm());
        model.addAttribute("roles", CarAccountRole.values());
        return "carlog/user-form";
    }

    @PostMapping("/users")
    public String createUser(@Valid @ModelAttribute("userForm") CarUserForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            bindingResult.rejectValue("password", "required", "Паролата е задължителна.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", CarAccountRole.values());
            return "carlog/user-form";
        }
        try {
            carUserService.create(form);
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("user", ex.getMessage());
            model.addAttribute("roles", CarAccountRole.values());
            return "carlog/user-form";
        }
        redirectAttributes.addFlashAttribute("success", "Потребителят е създаден.");
        return "redirect:/carlog/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        CarUser user = carUserService.findById(id).orElseThrow();
        model.addAttribute("userForm", mapUser(user));
        model.addAttribute("roles", CarAccountRole.values());
        model.addAttribute("userEntity", user);
        return "carlog/user-form";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("userForm") CarUserForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", CarAccountRole.values());
            model.addAttribute("userEntity", carUserService.findById(id).orElseThrow());
            return "carlog/user-form";
        }
        try {
            carUserService.update(id, form);
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("user", ex.getMessage());
            model.addAttribute("roles", CarAccountRole.values());
            model.addAttribute("userEntity", carUserService.findById(id).orElseThrow());
            return "carlog/user-form";
        }
        redirectAttributes.addFlashAttribute("success", "Потребителят е обновен.");
        return "redirect:/carlog/admin/users";
    }

    private void addCarMeta(Model model) {
        model.addAttribute("owners", carUserService.findAssignableOwners());
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("paymentPlans", PaymentPlan.values());
    }

    private CarForm mapCar(Car car) {
        CarForm form = new CarForm();
        form.setId(car.getId());
        form.setOwnerId(car.getOwner() != null ? car.getOwner().getId() : null);
        form.setProductionYear(car.getProductionYear());
        form.setMake(car.getMake());
        form.setModel(car.getModel());
        form.setFuelType(car.getFuelType());
        form.setMileage(car.getMileage());
        form.setRegistrationNumber(car.getRegistrationNumber());
        form.setNotesSummary(car.getNotesSummary());
        form.setAnnualTaxPaid(car.isAnnualTaxPaid());
        form.setLiabilityInsurer(car.getLiabilityInsurance().getInsurer());
        form.setLiabilityStartDate(car.getLiabilityInsurance().getStartDate());
        form.setLiabilityEndDate(car.getLiabilityInsurance().getEndDate());
        form.setLiabilityAmount(car.getLiabilityInsurance().getAmount());
        form.setLiabilityPaymentPlan(car.getLiabilityInsurance().getPaymentPlan());
        form.setCascoInsurer(car.getCascoInsurance().getInsurer());
        form.setCascoStartDate(car.getCascoInsurance().getStartDate());
        form.setCascoEndDate(car.getCascoInsurance().getEndDate());
        form.setCascoAmount(car.getCascoInsurance().getAmount());
        form.setCascoPaymentPlan(car.getCascoInsurance().getPaymentPlan());
        form.setInspectionDate(car.getTechnicalInspection().getInspectionDate());
        form.setInspectionValidUntil(car.getTechnicalInspection().getValidUntil());
        form.setVignetteStartDate(car.getVignette().getStartDate());
        form.setVignetteValidUntil(car.getVignette().getValidUntil());
        return form;
    }

    private CarUserForm mapUser(CarUser user) {
        CarUserForm form = new CarUserForm();
        form.setId(user.getId());
        form.setUsername(user.getUsername());
        form.setFullName(user.getFullName());
        form.setRole(user.getRole());
        form.setEnabled(user.isEnabled());
        return form;
    }
}
