package com.mytransport.models.carlog;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carlog_cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer productionYear;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    @Column(nullable = false)
    private Long mileage;

    @Column(nullable = false)
    private String registrationNumber;

    @Column(length = 1500)
    private String notesSummary;

    @Column(nullable = false)
    private boolean annualTaxPaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private CarUser owner;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "insurer", column = @Column(name = "liability_insurer")),
            @AttributeOverride(name = "startDate", column = @Column(name = "liability_start_date")),
            @AttributeOverride(name = "endDate", column = @Column(name = "liability_end_date")),
            @AttributeOverride(name = "amount", column = @Column(name = "liability_amount")),
            @AttributeOverride(name = "paymentPlan", column = @Column(name = "liability_payment_plan"))
    })
    private InsurancePolicyInfo liabilityInsurance = new InsurancePolicyInfo();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "insurer", column = @Column(name = "casco_insurer")),
            @AttributeOverride(name = "startDate", column = @Column(name = "casco_start_date")),
            @AttributeOverride(name = "endDate", column = @Column(name = "casco_end_date")),
            @AttributeOverride(name = "amount", column = @Column(name = "casco_amount")),
            @AttributeOverride(name = "paymentPlan", column = @Column(name = "casco_payment_plan"))
    })
    private InsurancePolicyInfo cascoInsurance = new InsurancePolicyInfo();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "inspectionDate", column = @Column(name = "inspection_date")),
            @AttributeOverride(name = "validUntil", column = @Column(name = "inspection_valid_until"))
    })
    private TechnicalInspectionInfo technicalInspection = new TechnicalInspectionInfo();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "vignette_start_date")),
            @AttributeOverride(name = "validUntil", column = @Column(name = "vignette_valid_until"))
    })
    private VignetteInfo vignette = new VignetteInfo();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("expenseDate DESC, createdAt DESC")
    private List<CarExpense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("serviceDate DESC, createdAt DESC")
    private List<CarServiceRecord> serviceRecords = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("entryDate DESC, createdAt DESC")
    private List<FuelEntry> fuelEntries = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<CarNote> notes = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dueDate ASC, createdAt DESC")
    private List<CarReminder> reminders = new ArrayList<>();

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getDisplayName() {
        return productionYear + " " + make + " " + model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CarUser getOwner() {
        return owner;
    }

    public void setOwner(CarUser owner) {
        this.owner = owner;
    }

    public InsurancePolicyInfo getLiabilityInsurance() {
        if (liabilityInsurance == null) {
            liabilityInsurance = new InsurancePolicyInfo();
        }
        return liabilityInsurance;
    }

    public void setLiabilityInsurance(InsurancePolicyInfo liabilityInsurance) {
        this.liabilityInsurance = liabilityInsurance;
    }

    public InsurancePolicyInfo getCascoInsurance() {
        if (cascoInsurance == null) {
            cascoInsurance = new InsurancePolicyInfo();
        }
        return cascoInsurance;
    }

    public void setCascoInsurance(InsurancePolicyInfo cascoInsurance) {
        this.cascoInsurance = cascoInsurance;
    }

    public TechnicalInspectionInfo getTechnicalInspection() {
        if (technicalInspection == null) {
            technicalInspection = new TechnicalInspectionInfo();
        }
        return technicalInspection;
    }

    public void setTechnicalInspection(TechnicalInspectionInfo technicalInspection) {
        this.technicalInspection = technicalInspection;
    }

    public VignetteInfo getVignette() {
        if (vignette == null) {
            vignette = new VignetteInfo();
        }
        return vignette;
    }

    public void setVignette(VignetteInfo vignette) {
        this.vignette = vignette;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<CarExpense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<CarExpense> expenses) {
        this.expenses = expenses;
    }

    public List<CarServiceRecord> getServiceRecords() {
        return serviceRecords;
    }

    public void setServiceRecords(List<CarServiceRecord> serviceRecords) {
        this.serviceRecords = serviceRecords;
    }

    public List<FuelEntry> getFuelEntries() {
        return fuelEntries;
    }

    public void setFuelEntries(List<FuelEntry> fuelEntries) {
        this.fuelEntries = fuelEntries;
    }

    public List<CarNote> getNotes() {
        return notes;
    }

    public void setNotes(List<CarNote> notes) {
        this.notes = notes;
    }

    public List<CarReminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<CarReminder> reminders) {
        this.reminders = reminders;
    }
}
