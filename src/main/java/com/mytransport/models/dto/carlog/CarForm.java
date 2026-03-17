package com.mytransport.models.dto.carlog;

import com.mytransport.models.carlog.FuelType;
import com.mytransport.models.carlog.PaymentPlan;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CarForm {

    private Long id;
    private Long ownerId;

    @NotNull
    @Min(1900)
    private Integer productionYear;

    @NotBlank
    @Size(max = 120)
    private String make;

    @NotBlank
    @Size(max = 120)
    private String model;

    @NotNull
    private FuelType fuelType;

    @NotNull
    @Min(0)
    private Long mileage;

    @NotBlank
    @Size(max = 30)
    private String registrationNumber;

    @Size(max = 1500)
    private String notesSummary;

    private boolean annualTaxPaid;
    private String liabilityInsurer;
    private LocalDate liabilityStartDate;
    private LocalDate liabilityEndDate;
    private BigDecimal liabilityAmount;
    private PaymentPlan liabilityPaymentPlan = PaymentPlan.SINGLE;
    private String cascoInsurer;
    private LocalDate cascoStartDate;
    private LocalDate cascoEndDate;
    private BigDecimal cascoAmount;
    private PaymentPlan cascoPaymentPlan = PaymentPlan.SINGLE;
    private LocalDate inspectionDate;
    private LocalDate inspectionValidUntil;
    private LocalDate vignetteStartDate;
    private LocalDate vignetteValidUntil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Long getMileage() {
        return mileage;
    }

    public void setMileage(Long mileage) {
        this.mileage = mileage;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getNotesSummary() {
        return notesSummary;
    }

    public void setNotesSummary(String notesSummary) {
        this.notesSummary = notesSummary;
    }

    public boolean isAnnualTaxPaid() {
        return annualTaxPaid;
    }

    public void setAnnualTaxPaid(boolean annualTaxPaid) {
        this.annualTaxPaid = annualTaxPaid;
    }

    public String getLiabilityInsurer() {
        return liabilityInsurer;
    }

    public void setLiabilityInsurer(String liabilityInsurer) {
        this.liabilityInsurer = liabilityInsurer;
    }

    public LocalDate getLiabilityStartDate() {
        return liabilityStartDate;
    }

    public void setLiabilityStartDate(LocalDate liabilityStartDate) {
        this.liabilityStartDate = liabilityStartDate;
    }

    public LocalDate getLiabilityEndDate() {
        return liabilityEndDate;
    }

    public void setLiabilityEndDate(LocalDate liabilityEndDate) {
        this.liabilityEndDate = liabilityEndDate;
    }

    public BigDecimal getLiabilityAmount() {
        return liabilityAmount;
    }

    public void setLiabilityAmount(BigDecimal liabilityAmount) {
        this.liabilityAmount = liabilityAmount;
    }

    public PaymentPlan getLiabilityPaymentPlan() {
        return liabilityPaymentPlan;
    }

    public void setLiabilityPaymentPlan(PaymentPlan liabilityPaymentPlan) {
        this.liabilityPaymentPlan = liabilityPaymentPlan;
    }

    public String getCascoInsurer() {
        return cascoInsurer;
    }

    public void setCascoInsurer(String cascoInsurer) {
        this.cascoInsurer = cascoInsurer;
    }

    public LocalDate getCascoStartDate() {
        return cascoStartDate;
    }

    public void setCascoStartDate(LocalDate cascoStartDate) {
        this.cascoStartDate = cascoStartDate;
    }

    public LocalDate getCascoEndDate() {
        return cascoEndDate;
    }

    public void setCascoEndDate(LocalDate cascoEndDate) {
        this.cascoEndDate = cascoEndDate;
    }

    public BigDecimal getCascoAmount() {
        return cascoAmount;
    }

    public void setCascoAmount(BigDecimal cascoAmount) {
        this.cascoAmount = cascoAmount;
    }

    public PaymentPlan getCascoPaymentPlan() {
        return cascoPaymentPlan;
    }

    public void setCascoPaymentPlan(PaymentPlan cascoPaymentPlan) {
        this.cascoPaymentPlan = cascoPaymentPlan;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public LocalDate getInspectionValidUntil() {
        return inspectionValidUntil;
    }

    public void setInspectionValidUntil(LocalDate inspectionValidUntil) {
        this.inspectionValidUntil = inspectionValidUntil;
    }

    public LocalDate getVignetteStartDate() {
        return vignetteStartDate;
    }

    public void setVignetteStartDate(LocalDate vignetteStartDate) {
        this.vignetteStartDate = vignetteStartDate;
    }

    public LocalDate getVignetteValidUntil() {
        return vignetteValidUntil;
    }

    public void setVignetteValidUntil(LocalDate vignetteValidUntil) {
        this.vignetteValidUntil = vignetteValidUntil;
    }
}
