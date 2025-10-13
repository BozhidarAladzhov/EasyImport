package com.mytransport.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ContactForm {

    @NotBlank(message="Моля, въведете име") private String name;
    @Email @NotBlank(message="Моля, въведете валиден email") private String email;
    @NotBlank(message = "Моля, въведете Година/Марка/Модел.")
    @Size(max = 300, message = "Полето може да съдържа до 300 символа.")
    private String vehicleDetails;
    @NotBlank private String originPort;
    @NotBlank private String destinationPort;
    @NotBlank private String vehicleType;
    private boolean isHybrid;
    private boolean isElectric;

    private String website;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public String getOriginPort() {
        return originPort;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isHybrid() {
        return isHybrid;
    }

    public void setHybrid(boolean hybrid) {
        isHybrid = hybrid;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }
}
