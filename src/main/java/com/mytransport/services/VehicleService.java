package com.mytransport.services;

import com.mytransport.models.PhotoKind;
import com.mytransport.models.Vehicle;
import com.mytransport.models.VehiclePhoto;
import com.mytransport.models.VehicleStatus;
import com.mytransport.models.VehicleStatusUpdate;
import com.mytransport.models.VehicleDocument;
import com.mytransport.models.DocumentKind;
import com.mytransport.models.dto.VehicleForm;
import com.mytransport.repository.VehicleDocumentRepository;
import com.mytransport.repository.VehiclePhotoRepository;
import com.mytransport.repository.VehicleRepository;
import com.mytransport.repository.VehicleStatusUpdateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleService {

    private static final int TOKEN_VALIDITY_MONTHS = 6;

    private final VehicleRepository vehicleRepository;
    private final VehiclePhotoRepository photoRepository;
    private final VehicleStatusUpdateRepository statusUpdateRepository;
    private final VehicleDocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final MailService mailService;

    public VehicleService(VehicleRepository vehicleRepository,
                          VehiclePhotoRepository photoRepository,
                          VehicleStatusUpdateRepository statusUpdateRepository,
                          VehicleDocumentRepository documentRepository,
                          FileStorageService fileStorageService,
                          MailService mailService) {
        this.vehicleRepository = vehicleRepository;
        this.photoRepository = photoRepository;
        this.statusUpdateRepository = statusUpdateRepository;
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
        this.mailService = mailService;
    }

    public List<Vehicle> listAll() {
        return vehicleRepository.findAllByOrderByUpdatedAtDesc();
    }

    public List<Vehicle> listFiltered(String query, VehicleStatus status) {
        boolean hasQuery = query != null && !query.trim().isEmpty();
        if (!hasQuery && status == null) {
            return listAll();
        }
        String q = hasQuery ? query.trim() : null;
        return vehicleRepository.search(q, status);
    }

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Optional<Vehicle> findByValidToken(String token) {
        return vehicleRepository.findByPublicTokenAndPublicTokenExpiresAtAfter(token, LocalDateTime.now());
    }

    public Optional<Vehicle> findByToken(String token) {
        return vehicleRepository.findByPublicToken(token);
    }

    @Transactional
    public Vehicle createVehicle(VehicleForm form, MultipartFile titleScan) throws IOException {
        Vehicle vehicle = new Vehicle();
        applyForm(vehicle, form);
        vehicle = vehicleRepository.save(vehicle);

        generateOrRefreshToken(vehicle);
        addStatusUpdate(vehicle, vehicle.getStatus(), form.getStatusNote());

        if (titleScan != null && !titleScan.isEmpty()) {
            updateTitleScan(vehicle, titleScan);
        }

        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle updateVehicle(Long id, VehicleForm form, MultipartFile titleScan) throws IOException {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        VehicleStatus previousStatus = vehicle.getStatus();
        applyForm(vehicle, form);

        if (statusChanged(previousStatus, vehicle.getStatus()) || hasNote(form.getStatusNote())) {
            addStatusUpdate(vehicle, vehicle.getStatus(), form.getStatusNote());
            sendStatusEmail(vehicle, form.getStatusNote());
        }

        if (titleScan != null && !titleScan.isEmpty()) {
            updateTitleScan(vehicle, titleScan);
        }

        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (vehicle.getTitleScanPath() != null) {
            fileStorageService.deleteIfExists(vehicle.getTitleScanPath());
        }

        for (VehiclePhoto photo : vehicle.getPhotos()) {
            fileStorageService.deleteIfExists(photo.getFilePath());
        }

        vehicleRepository.delete(vehicle);
    }

    @Transactional
    public Vehicle regenerateToken(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        generateOrRefreshToken(vehicle);
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void removeTitleScan(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        if (vehicle.getTitleScanPath() != null) {
            fileStorageService.deleteIfExists(vehicle.getTitleScanPath());
        }
        vehicle.setTitleScanPath(null);
        vehicle.setTitleScanOriginalName(null);
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public VehiclePhoto addPhoto(Long vehicleId, PhotoKind kind, MultipartFile file) throws IOException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        String path = fileStorageService.storePhoto(vehicleId, file);

        VehiclePhoto photo = new VehiclePhoto();
        photo.setVehicle(vehicle);
        photo.setKind(kind);
        photo.setFilePath(path);
        photo.setOriginalName(file.getOriginalFilename());

        return photoRepository.save(photo);
    }

    @Transactional
    public void deletePhoto(Long photoId) {
        VehiclePhoto photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Photo not found"));
        fileStorageService.deleteIfExists(photo.getFilePath());
        photoRepository.delete(photo);
    }

    public List<VehiclePhoto> listPhotos(Long vehicleId) {
        return photoRepository.findByVehicleId(vehicleId);
    }

    public Optional<VehiclePhoto> findPhoto(Long photoId) {
        return photoRepository.findById(photoId);
    }

    public Optional<VehiclePhoto> findPhotoForVehicle(Long photoId, Long vehicleId) {
        return photoRepository.findByIdAndVehicleId(photoId, vehicleId);
    }

    public List<VehicleStatusUpdate> listStatusUpdates(Long vehicleId) {
        return statusUpdateRepository.findByVehicleIdOrderByCreatedAtDesc(vehicleId);
    }

    public List<VehicleDocument> listDocuments(Long vehicleId) {
        return documentRepository.findByVehicleIdOrderByUploadedAtDesc(vehicleId);
    }

    public Optional<VehicleDocument> findDocument(Long docId) {
        return documentRepository.findById(docId);
    }

    public Optional<VehicleDocument> findDocumentForVehicle(Long docId, Long vehicleId) {
        return documentRepository.findByIdAndVehicleId(docId, vehicleId);
    }

    @Transactional
    public VehicleDocument addDocument(Long vehicleId, DocumentKind kind, String title, MultipartFile file) throws IOException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        String path = fileStorageService.storeDocument(vehicleId, file);

        VehicleDocument doc = new VehicleDocument();
        doc.setVehicle(vehicle);
        doc.setKind(kind);
        doc.setTitle(title);
        doc.setFilePath(path);
        doc.setOriginalName(file.getOriginalFilename());

        VehicleDocument saved = documentRepository.save(doc);
        sendDocumentEmail(vehicle, saved);
        return saved;
    }

    @Transactional
    public void deleteDocument(Long docId) {
        VehicleDocument doc = documentRepository.findById(docId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        fileStorageService.deleteIfExists(doc.getFilePath());
        documentRepository.delete(doc);
    }

    private void applyForm(Vehicle vehicle, VehicleForm form) {
        vehicle.setVehicleName(form.getVehicleName());
        vehicle.setClientName(form.getClientName());
        vehicle.setClientEmail(form.getClientEmail());
        vehicle.setVin(form.getVin());
        vehicle.setContainerNumber(form.getContainerNumber());
        vehicle.setTrackingUrl(form.getTrackingUrl());
        vehicle.setPol(form.getPol());
        vehicle.setPod(form.getPod());
        vehicle.setEta(form.getEta());
        vehicle.setKeysCount(form.getKeysCount());
        vehicle.setStatus(form.getStatus());
        vehicle.setCustomsCleared(Boolean.TRUE.equals(form.getCustomsCleared()));
        vehicle.setPaid(Boolean.TRUE.equals(form.getPaid()));
        vehicle.setTitleAvailable(Boolean.TRUE.equals(form.getTitleAvailable()));
    }

    private void updateTitleScan(Vehicle vehicle, MultipartFile titleScan) throws IOException {
        if (vehicle.getTitleScanPath() != null) {
            fileStorageService.deleteIfExists(vehicle.getTitleScanPath());
        }

        String path = fileStorageService.storeTitleScan(vehicle.getId(), titleScan);
        vehicle.setTitleScanPath(path);
        vehicle.setTitleScanOriginalName(titleScan.getOriginalFilename());
    }

    private void generateOrRefreshToken(Vehicle vehicle) {
        vehicle.setPublicToken(UUID.randomUUID().toString().replace("-", ""));
        vehicle.setPublicTokenCreatedAt(LocalDateTime.now());
        vehicle.setPublicTokenExpiresAt(LocalDateTime.now().plusMonths(TOKEN_VALIDITY_MONTHS));
    }

    private void addStatusUpdate(Vehicle vehicle, VehicleStatus status, String note) {
        VehicleStatusUpdate update = new VehicleStatusUpdate();
        update.setVehicle(vehicle);
        update.setStatus(status);
        if (hasNote(note)) {
            update.setNote(note.trim());
        }
        statusUpdateRepository.save(update);
    }

    private boolean statusChanged(VehicleStatus previous, VehicleStatus current) {
        if (previous == null && current == null) {
            return false;
        }
        if (previous == null) {
            return true;
        }
        return !previous.equals(current);
    }

    private boolean hasNote(String note) {
        return note != null && !note.trim().isEmpty();
    }

    private void sendStatusEmail(Vehicle vehicle, String note) {
        if (vehicle.getClientEmail() == null || vehicle.getClientEmail().isBlank()) {
            return;
        }
        String subject = "Status update: " + vehicle.getVehicleName();
        StringBuilder body = new StringBuilder();
        body.append("Vehicle: ").append(vehicle.getVehicleName()).append("\n");
        if (vehicle.getVin() != null && !vehicle.getVin().isBlank()) {
            body.append("VIN: ").append(vehicle.getVin()).append("\n");
        }
        body.append("Status: ").append(vehicle.getStatus().getLabel()).append("\n");
        if (hasNote(note)) {
            body.append("Note: ").append(note.trim()).append("\n");
        }
        mailService.sendEmail(vehicle.getClientEmail(), subject, body.toString());
    }

    private void sendDocumentEmail(Vehicle vehicle, VehicleDocument doc) {
        if (vehicle.getClientEmail() == null || vehicle.getClientEmail().isBlank()) {
            return;
        }
        String subject = "New document: " + vehicle.getVehicleName();
        StringBuilder body = new StringBuilder();
        body.append("Vehicle: ").append(vehicle.getVehicleName()).append("\n");
        if (vehicle.getVin() != null && !vehicle.getVin().isBlank()) {
            body.append("VIN: ").append(vehicle.getVin()).append("\n");
        }
        body.append("Document: ").append(doc.getKind().getLabel()).append("\n");
        if (doc.getTitle() != null && !doc.getTitle().isBlank()) {
            body.append("Title: ").append(doc.getTitle()).append("\n");
        }
        mailService.sendEmail(vehicle.getClientEmail(), subject, body.toString());
    }
}
