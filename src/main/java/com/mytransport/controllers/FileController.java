package com.mytransport.controllers;

import com.mytransport.models.Vehicle;
import com.mytransport.models.VehiclePhoto;
import com.mytransport.models.VehicleDocument;
import com.mytransport.services.FileStorageService;
import com.mytransport.services.VehicleService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class FileController {

    private final VehicleService vehicleService;
    private final FileStorageService fileStorageService;

    public FileController(VehicleService vehicleService, FileStorageService fileStorageService) {
        this.vehicleService = vehicleService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/admin/files/title/{vehicleId}")
    public ResponseEntity<Resource> adminTitle(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId).orElseThrow();
        return fileResponse(vehicle.getTitleScanPath(), vehicle.getTitleScanOriginalName());
    }

    @GetMapping("/admin/files/photo/{photoId}")
    public ResponseEntity<Resource> adminPhoto(@PathVariable Long photoId) {
        VehiclePhoto photo = vehicleService.findPhoto(photoId).orElseThrow();
        return fileResponse(photo.getFilePath(), photo.getOriginalName());
    }

    @GetMapping("/admin/files/doc/{docId}")
    public ResponseEntity<Resource> adminDocument(@PathVariable Long docId) {
        VehicleDocument doc = vehicleService.findDocument(docId).orElseThrow();
        return fileResponse(doc.getFilePath(), doc.getOriginalName());
    }

    @GetMapping("/admin/vehicles/{vehicleId}/photos.zip")
    public ResponseEntity<StreamingResponseBody> adminPhotosZip(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId).orElseThrow();
        List<VehiclePhoto> photos = vehicleService.listPhotos(vehicleId);
        return zipPhotosResponse(vehicle.getVehicleName(), photos);
    }

    @GetMapping("/v/{token}/title")
    public ResponseEntity<Resource> publicTitle(@PathVariable String token) {
        Vehicle vehicle = vehicleService.findByValidToken(token).orElseThrow();
        return fileResponse(vehicle.getTitleScanPath(), vehicle.getTitleScanOriginalName());
    }

    @GetMapping("/v/{token}/photo/{photoId}")
    public ResponseEntity<Resource> publicPhoto(@PathVariable String token, @PathVariable Long photoId) {
        Vehicle vehicle = vehicleService.findByValidToken(token).orElseThrow();
        VehiclePhoto photo = vehicleService.findPhotoForVehicle(photoId, vehicle.getId()).orElse(null);
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }
        return fileResponse(photo.getFilePath(), photo.getOriginalName());
    }

    @GetMapping("/v/{token}/doc/{docId}")
    public ResponseEntity<Resource> publicDocument(@PathVariable String token, @PathVariable Long docId) {
        Vehicle vehicle = vehicleService.findByValidToken(token).orElseThrow();
        VehicleDocument doc = vehicleService.findDocumentForVehicle(docId, vehicle.getId()).orElse(null);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }
        return fileResponse(doc.getFilePath(), doc.getOriginalName());
    }

    @GetMapping("/v/{token}/photos.zip")
    public ResponseEntity<StreamingResponseBody> publicPhotosZip(@PathVariable String token) {
        Vehicle vehicle = vehicleService.findByValidToken(token).orElseThrow();
        List<VehiclePhoto> photos = vehicleService.listPhotos(vehicle.getId());
        return zipPhotosResponse(vehicle.getVehicleName(), photos);
    }

    private ResponseEntity<Resource> fileResponse(String path, String filename) {
        if (path == null || path.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = fileStorageService.loadAsResource(path);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        try {
            Path filePath = resource.getFile().toPath();
            String detected = Files.probeContentType(filePath);
            if (detected != null) {
                mediaType = MediaType.parseMediaType(detected);
            }
        } catch (Exception ignored) {
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + safeFilename(filename) + "\"")
                .body(resource);
    }

    private String safeFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "file";
        }
        return filename.replace("\"", "");
    }

    private ResponseEntity<StreamingResponseBody> zipPhotosResponse(String vehicleName, List<VehiclePhoto> photos) {
        String zipName = safeFilename((vehicleName == null || vehicleName.isBlank()) ? "photos" : vehicleName) + "-photos.zip";
        StreamingResponseBody body = outputStream -> {
            try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
                for (VehiclePhoto photo : photos) {
                    if (photo.getFilePath() == null) {
                        continue;
                    }
                    Path filePath = fileStorageService.resolvePath(photo.getFilePath());
                    if (!Files.exists(filePath)) {
                        continue;
                    }
                    String entryName = safeFilename(photo.getOriginalName());
                    if (entryName.equals("file")) {
                        entryName = "photo-" + photo.getId();
                    } else {
                        entryName = photo.getId() + "-" + entryName;
                    }
                    zip.putNextEntry(new ZipEntry(entryName));
                    Files.copy(filePath, zip);
                    zip.closeEntry();
                }
                zip.finish();
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(body);
    }
}
