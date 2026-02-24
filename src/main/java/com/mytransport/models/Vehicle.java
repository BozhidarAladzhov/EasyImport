package com.mytransport.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehicleName;

    private String clientName;
    private String clientEmail;

    private String vin;
    private String containerNumber;
    private String trackingUrl;
    private String pol;
    private String pod;
    private LocalDate eta;
    private Integer keysCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status = VehicleStatus.IN_AUCTION;

    private Boolean customsCleared = Boolean.FALSE;
    private Boolean paid = Boolean.FALSE;

    private Boolean titleAvailable = Boolean.FALSE;
    private String titleScanPath;
    private String titleScanOriginalName;

    @Column(unique = true)
    private String publicToken;

    private LocalDateTime publicTokenExpiresAt;
    private LocalDateTime publicTokenCreatedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiclePhoto> photos = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleStatusUpdate> statusUpdates = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleDocument> documents = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public LocalDate getEta() {
        return eta;
    }

    public void setEta(LocalDate eta) {
        this.eta = eta;
    }

    public Integer getKeysCount() {
        return keysCount;
    }

    public void setKeysCount(Integer keysCount) {
        this.keysCount = keysCount;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public Boolean getCustomsCleared() {
        return customsCleared;
    }

    public void setCustomsCleared(Boolean customsCleared) {
        this.customsCleared = customsCleared;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getTitleAvailable() {
        return titleAvailable;
    }

    public void setTitleAvailable(Boolean titleAvailable) {
        this.titleAvailable = titleAvailable;
    }

    public String getTitleScanPath() {
        return titleScanPath;
    }

    public void setTitleScanPath(String titleScanPath) {
        this.titleScanPath = titleScanPath;
    }

    public String getTitleScanOriginalName() {
        return titleScanOriginalName;
    }

    public void setTitleScanOriginalName(String titleScanOriginalName) {
        this.titleScanOriginalName = titleScanOriginalName;
    }

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public LocalDateTime getPublicTokenExpiresAt() {
        return publicTokenExpiresAt;
    }

    public void setPublicTokenExpiresAt(LocalDateTime publicTokenExpiresAt) {
        this.publicTokenExpiresAt = publicTokenExpiresAt;
    }

    public LocalDateTime getPublicTokenCreatedAt() {
        return publicTokenCreatedAt;
    }

    public void setPublicTokenCreatedAt(LocalDateTime publicTokenCreatedAt) {
        this.publicTokenCreatedAt = publicTokenCreatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<VehiclePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<VehiclePhoto> photos) {
        this.photos = photos;
    }

    public List<VehicleStatusUpdate> getStatusUpdates() {
        return statusUpdates;
    }

    public void setStatusUpdates(List<VehicleStatusUpdate> statusUpdates) {
        this.statusUpdates = statusUpdates;
    }

    public List<VehicleDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<VehicleDocument> documents) {
        this.documents = documents;
    }
}
