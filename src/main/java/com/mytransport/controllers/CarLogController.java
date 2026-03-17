package com.mytransport.controllers;

import com.mytransport.models.carlog.Car;
import com.mytransport.services.CarLogService;
import com.mytransport.services.CarUserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/carlog")
public class CarLogController {

    private final CarLogService carLogService;
    private final CarUserService carUserService;

    public CarLogController(CarLogService carLogService, CarUserService carUserService) {
        this.carLogService = carLogService;
        this.carUserService = carUserService;
    }

    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        boolean admin = isAdmin(authentication);
        List<Car> cars = carLogService.findVisibleCars(authentication.getName(), admin);
        model.addAttribute("cars", cars);
        model.addAttribute("isAdmin", admin);
        model.addAttribute("displayName", resolveDisplayName(authentication));
        return "carlog/dashboard";
    }

    @GetMapping("/cars/{id}")
    public String detail(@PathVariable Long id, Authentication authentication, Model model) {
        Car car = carLogService.findVisibleCar(id, authentication.getName(), isAdmin(authentication)).orElseThrow();
        model.addAttribute("car", car);
        model.addAttribute("isAdmin", isAdmin(authentication));
        model.addAttribute("displayName", resolveDisplayName(authentication));
        return "carlog/car-detail";
    }

    @PostMapping("/cars/{id}/notes")
    public String addNote(@PathVariable Long id,
                          @RequestParam String content,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        carLogService.findVisibleCar(id, authentication.getName(), isAdmin(authentication)).orElseThrow();
        carLogService.addNote(id, content, resolveDisplayName(authentication));
        redirectAttributes.addFlashAttribute("success", "Бележката е добавена.");
        return "redirect:/carlog/cars/" + id;
    }

    @PostMapping("/cars/{id}/expenses")
    public String addExpense(@PathVariable Long id,
                             @RequestParam String category,
                             @RequestParam BigDecimal amount,
                             @RequestParam LocalDate expenseDate,
                             @RequestParam(required = false) String description,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        requireAdmin(authentication);
        carLogService.addExpense(id, category, amount, expenseDate, description, resolveDisplayName(authentication));
        redirectAttributes.addFlashAttribute("success", "Разходът е добавен.");
        return "redirect:/carlog/cars/" + id;
    }

    @PostMapping("/cars/{id}/service-records")
    public String addServiceRecord(@PathVariable Long id,
                                   @RequestParam LocalDate serviceDate,
                                   @RequestParam String title,
                                   @RequestParam(required = false) String details,
                                   @RequestParam(required = false) BigDecimal cost,
                                   @RequestParam(required = false) Long mileage,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        requireAdmin(authentication);
        carLogService.addServiceRecord(id, serviceDate, title, details, cost, mileage, resolveDisplayName(authentication));
        redirectAttributes.addFlashAttribute("success", "Обслужването е добавено.");
        return "redirect:/carlog/cars/" + id;
    }

    @PostMapping("/cars/{id}/fuel")
    public String addFuelEntry(@PathVariable Long id,
                               @RequestParam LocalDate entryDate,
                               @RequestParam BigDecimal liters,
                               @RequestParam BigDecimal totalCost,
                               @RequestParam(required = false) Long mileage,
                               @RequestParam(required = false) String station,
                               @RequestParam(required = false) String notes,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        requireAdmin(authentication);
        carLogService.addFuelEntry(id, entryDate, liters, totalCost, mileage, station, notes, resolveDisplayName(authentication));
        redirectAttributes.addFlashAttribute("success", "Зареждането е добавено.");
        return "redirect:/carlog/cars/" + id;
    }

    @PostMapping("/cars/{id}/reminders")
    public String addReminder(@PathVariable Long id,
                              @RequestParam String title,
                              @RequestParam(required = false) String details,
                              @RequestParam LocalDate dueDate,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        requireAdmin(authentication);
        carLogService.addReminder(id, title, details, dueDate, resolveDisplayName(authentication));
        redirectAttributes.addFlashAttribute("success", "Напомнянето е добавено.");
        return "redirect:/carlog/cars/" + id;
    }

    @PostMapping("/cars/{carId}/reminders/{reminderId}/toggle")
    public String toggleReminder(@PathVariable Long carId,
                                 @PathVariable Long reminderId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        requireAdmin(authentication);
        carLogService.toggleReminder(reminderId);
        redirectAttributes.addFlashAttribute("success", "Напомнянето е обновено.");
        return "redirect:/carlog/cars/" + carId;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    private void requireAdmin(Authentication authentication) {
        if (!isAdmin(authentication)) {
            throw new AccessDeniedException("Admin access required.");
        }
    }

    private String resolveDisplayName(Authentication authentication) {
        return carUserService.findByUsername(authentication.getName())
                .map(user -> user.getFullName() + " (" + user.getUsername() + ")")
                .orElse(authentication.getName());
    }
}
