package com.mytransport.models.carlog;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class VignetteInfo {

    private LocalDate startDate;
    private LocalDate validUntil;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }
}
