package com.mytransport.controllers;

import com.mytransport.models.PhotoKind;
import com.mytransport.models.DocumentKind;
import com.mytransport.models.Vehicle;
import com.mytransport.models.VehicleStatus;
import com.mytransport.models.dto.VehicleForm;
import com.mytransport.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {

    private final VehicleService vehicleService;

    public AdminVehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String query,
                       @RequestParam(value = "status", required = false) VehicleStatus status,
                       Model model) {
        model.addAttribute("vehicles", vehicleService.listFiltered(query, status));
        model.addAttribute("q", query);
        model.addAttribute("status", status);
        model.addAttribute("statusOptions", VehicleStatus.values());
        return "admin/vehicles";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("vehicleForm", new VehicleForm());
        addCommonFormData(model);
        return "admin/vehicle-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("vehicleForm") VehicleForm form,
                         BindingResult br,
                         @RequestParam(value = "titleScan", required = false) MultipartFile titleScan,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (br.hasErrors()) {
            addCommonFormData(model);
            return "admin/vehicle-form";
        }
        try {
            Vehicle vehicle = vehicleService.createVehicle(form, titleScan);
            redirectAttributes.addFlashAttribute("success", "Vehicle created.");
            return "redirect:/admin/vehicles/" + vehicle.getId() + "/edit";
        } catch (IOException e) {
            addCommonFormData(model);
            model.addAttribute("error", "Failed to upload title scan.");
            return "admin/vehicle-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id).orElseThrow();
        VehicleForm form = mapToForm(vehicle);
        model.addAttribute("vehicleForm", form);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("statusUpdates", vehicleService.listStatusUpdates(id));
        addCommonFormData(model);
        return "admin/vehicle-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("vehicleForm") VehicleForm form,
                         BindingResult br,
                         @RequestParam(value = "titleScan", required = false) MultipartFile titleScan,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (br.hasErrors()) {
            model.addAttribute("vehicle", vehicleService.findById(id).orElseThrow());
            model.addAttribute("statusUpdates", vehicleService.listStatusUpdates(id));
            addCommonFormData(model);
            return "admin/vehicle-form";
        }
        try {
            vehicleService.updateVehicle(id, form, titleScan);
            redirectAttributes.addFlashAttribute("success", "Vehicle updated.");
            return "redirect:/admin/vehicles/" + id + "/edit";
        } catch (IOException e) {
            model.addAttribute("vehicle", vehicleService.findById(id).orElseThrow());
            model.addAttribute("statusUpdates", vehicleService.listStatusUpdates(id));
            addCommonFormData(model);
            model.addAttribute("error", "Failed to upload title scan.");
            return "admin/vehicle-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        vehicleService.deleteVehicle(id);
        redirectAttributes.addFlashAttribute("success", "Vehicle deleted.");
        return "redirect:/admin/vehicles";
    }

    @PostMapping("/{id}/regenerate-link")
    public String regenerate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        vehicleService.regenerateToken(id);
        redirectAttributes.addFlashAttribute("success", "Public link regenerated.");
        return "redirect:/admin/vehicles/" + id + "/edit";
    }

    @PostMapping("/{id}/title/remove")
    public String removeTitle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        vehicleService.removeTitleScan(id);
        redirectAttributes.addFlashAttribute("success", "Title scan removed.");
        return "redirect:/admin/vehicles/" + id + "/edit";
    }

    @GetMapping("/{id}/photos")
    public String photos(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id).orElseThrow();
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("photos", vehicleService.listPhotos(id));
        model.addAttribute("photoKinds", PhotoKind.values());
        return "admin/vehicle-photos";
    }

    @GetMapping("/{id}/docs")
    public String documents(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id).orElseThrow();
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("documents", vehicleService.listDocuments(id));
        model.addAttribute("docKinds", DocumentKind.values());
        return "admin/vehicle-documents";
    }

    @PostMapping("/{id}/photos")
    public String uploadPhotos(@PathVariable Long id,
                               @RequestParam("kind") PhotoKind kind,
                               @RequestParam("files") List<MultipartFile> files,
                               RedirectAttributes redirectAttributes) {
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            try {
                vehicleService.addPhoto(id, kind, file);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload one of the photos.");
                return "redirect:/admin/vehicles/" + id + "/photos";
            }
        }
        redirectAttributes.addFlashAttribute("success", "Photos uploaded.");
        return "redirect:/admin/vehicles/" + id + "/photos";
    }

    @PostMapping("/{id}/docs")
    public String uploadDocument(@PathVariable Long id,
                                 @RequestParam("kind") DocumentKind kind,
                                 @RequestParam(value = "title", required = false) String title,
                                 @RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "File is required.");
            return "redirect:/admin/vehicles/" + id + "/docs";
        }
        try {
            vehicleService.addDocument(id, kind, title, file);
            redirectAttributes.addFlashAttribute("success", "Document uploaded.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload document.");
        }
        return "redirect:/admin/vehicles/" + id + "/docs";
    }

    @PostMapping("/{vehicleId}/photos/{photoId}/delete")
    public String deletePhoto(@PathVariable Long vehicleId,
                              @PathVariable Long photoId,
                              RedirectAttributes redirectAttributes) {
        vehicleService.deletePhoto(photoId);
        redirectAttributes.addFlashAttribute("success", "Photo deleted.");
        return "redirect:/admin/vehicles/" + vehicleId + "/photos";
    }

    @PostMapping("/{vehicleId}/docs/{docId}/delete")
    public String deleteDocument(@PathVariable Long vehicleId,
                                 @PathVariable Long docId,
                                 RedirectAttributes redirectAttributes) {
        vehicleService.deleteDocument(docId);
        redirectAttributes.addFlashAttribute("success", "Document deleted.");
        return "redirect:/admin/vehicles/" + vehicleId + "/docs";
    }

    private void addCommonFormData(Model model) {
        model.addAttribute("statusOptions", VehicleStatus.values());
    }

    private VehicleForm mapToForm(Vehicle vehicle) {
        VehicleForm form = new VehicleForm();
        form.setId(vehicle.getId());
        form.setVehicleName(vehicle.getVehicleName());
        form.setClientName(vehicle.getClientName());
        form.setClientEmail(vehicle.getClientEmail());
        form.setVin(vehicle.getVin());
        form.setContainerNumber(vehicle.getContainerNumber());
        form.setTrackingUrl(vehicle.getTrackingUrl());
        form.setPol(vehicle.getPol());
        form.setPod(vehicle.getPod());
        form.setEta(vehicle.getEta());
        form.setKeysCount(vehicle.getKeysCount());
        form.setStatus(vehicle.getStatus());
        form.setCustomsCleared(vehicle.getCustomsCleared());
        form.setPaid(vehicle.getPaid());
        form.setTitleAvailable(vehicle.getTitleAvailable());
        return form;
    }
}
