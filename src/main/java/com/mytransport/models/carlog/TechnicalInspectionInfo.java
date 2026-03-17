package com.mytransport.models.carlog;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class TechnicalInspectionInfo {

    private LocalDate inspectionDate;
    private LocalDate validUntil;

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }
}
