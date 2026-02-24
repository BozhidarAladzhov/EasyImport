package com.mytransport.models.dto;

import com.mytransport.models.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VehicleForm {

    private Long id;

    @NotBlank(message = "Vehicle is required.")
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

    @NotNull(message = "Status is required.")
    private VehicleStatus status = VehicleStatus.IN_AUCTION;

    private Boolean customsCleared = Boolean.FALSE;
    private Boolean paid = Boolean.FALSE;
    private Boolean titleAvailable = Boolean.FALSE;

    private String statusNote;

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

    public String getStatusNote() {
        return statusNote;
    }

    public void setStatusNote(String statusNote) {
        this.statusNote = statusNote;
    }
}
