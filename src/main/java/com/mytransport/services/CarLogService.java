package com.mytransport.services;

import com.mytransport.models.carlog.*;
import com.mytransport.models.dto.carlog.CarForm;
import com.mytransport.repository.carlog.CarReminderRepository;
import com.mytransport.repository.carlog.CarRepository;
import com.mytransport.repository.carlog.CarUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CarLogService {

    private final CarRepository carRepository;
    private final CarUserRepository carUserRepository;
    private final CarReminderRepository carReminderRepository;

    public CarLogService(CarRepository carRepository,
                         CarUserRepository carUserRepository,
                         CarReminderRepository carReminderRepository) {
        this.carRepository = carRepository;
        this.carUserRepository = carUserRepository;
        this.carReminderRepository = carReminderRepository;
    }

    @Transactional(readOnly = true)
    public List<Car> findVisibleCars(String username, boolean admin) {
        return admin ? carRepository.findAllByOrderByCreatedAtDesc()
                : carRepository.findAllByOwnerUsernameOrderByCreatedAtDesc(username);
    }

    @Transactional(readOnly = true)
    public Optional<Car> findVisibleCar(Long carId, String username, boolean admin) {
        return admin ? carRepository.findById(carId) : carRepository.findByIdAndOwnerUsername(carId, username);
    }

    @Transactional(readOnly = true)
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    @Transactional
    public Car createCar(CarForm form) {
        Car car = new Car();
        apply(car, form);
        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(Long id, CarForm form) {
        Car car = carRepository.findById(id).orElseThrow();
        apply(car, form);
        return carRepository.save(car);
    }

    @Transactional
    public void addExpense(Long carId, String category, BigDecimal amount, LocalDate expenseDate, String description, String createdBy) {
        Car car = carRepository.findById(carId).orElseThrow();
        CarExpense expense = new CarExpense();
        expense.setCar(car);
        expense.setCategory(category);
        expense.setAmount(amount);
        expense.setExpenseDate(expenseDate);
        expense.setDescription(description);
        expense.setCreatedBy(createdBy);
        car.getExpenses().add(expense);
        carRepository.save(car);
    }

    @Transactional
    public void addServiceRecord(Long carId, LocalDate serviceDate, String title, String details, BigDecimal cost, Long mileage, String createdBy) {
        Car car = carRepository.findById(carId).orElseThrow();
        CarServiceRecord record = new CarServiceRecord();
        record.setCar(car);
        record.setServiceDate(serviceDate);
        record.setTitle(title);
        record.setDetails(details);
        record.setCost(cost);
        record.setMileage(mileage);
        record.setCreatedBy(createdBy);
        car.getServiceRecords().add(record);
        carRepository.save(car);
    }

    @Transactional
    public void addFuelEntry(Long carId, LocalDate entryDate, BigDecimal liters, BigDecimal totalCost, Long mileage, String station, String notes, String createdBy) {
        Car car = carRepository.findById(carId).orElseThrow();
        FuelEntry entry = new FuelEntry();
        entry.setCar(car);
        entry.setEntryDate(entryDate);
        entry.setLiters(liters);
        entry.setTotalCost(totalCost);
        entry.setMileage(mileage);
        entry.setStation(station);
        entry.setNotes(notes);
        entry.setCreatedBy(createdBy);
        car.getFuelEntries().add(entry);
        if (mileage != null && mileage > car.getMileage()) {
            car.setMileage(mileage);
        }
        carRepository.save(car);
    }

    @Transactional
    public void addNote(Long carId, String content, String createdBy) {
        Car car = carRepository.findById(carId).orElseThrow();
        CarNote note = new CarNote();
        note.setCar(car);
        note.setContent(content);
        note.setCreatedBy(createdBy);
        car.getNotes().add(note);
        carRepository.save(car);
    }

    @Transactional
    public void addReminder(Long carId, String title, String details, LocalDate dueDate, String createdBy) {
        Car car = carRepository.findById(carId).orElseThrow();
        CarReminder reminder = new CarReminder();
        reminder.setCar(car);
        reminder.setTitle(title);
        reminder.setDetails(details);
        reminder.setDueDate(dueDate);
        reminder.setCreatedBy(createdBy);
        car.getReminders().add(reminder);
        carRepository.save(car);
    }

    @Transactional
    public void toggleReminder(Long reminderId) {
        CarReminder reminder = carReminderRepository.findById(reminderId).orElseThrow();
        reminder.setCompleted(!reminder.isCompleted());
        carReminderRepository.save(reminder);
    }

    private void apply(Car car, CarForm form) {
        car.setProductionYear(form.getProductionYear());
        car.setMake(form.getMake().trim());
        car.setModel(form.getModel().trim());
        car.setFuelType(form.getFuelType());
        car.setMileage(form.getMileage());
        car.setRegistrationNumber(form.getRegistrationNumber().trim().toUpperCase());
        car.setNotesSummary(form.getNotesSummary());
        car.setAnnualTaxPaid(form.isAnnualTaxPaid());
        car.setOwner(form.getOwnerId() == null ? null : carUserRepository.findById(form.getOwnerId()).orElseThrow());

        car.getLiabilityInsurance().setInsurer(form.getLiabilityInsurer());
        car.getLiabilityInsurance().setStartDate(form.getLiabilityStartDate());
        car.getLiabilityInsurance().setEndDate(form.getLiabilityEndDate());
        car.getLiabilityInsurance().setAmount(form.getLiabilityAmount());
        car.getLiabilityInsurance().setPaymentPlan(form.getLiabilityPaymentPlan());

        car.getCascoInsurance().setInsurer(form.getCascoInsurer());
        car.getCascoInsurance().setStartDate(form.getCascoStartDate());
        car.getCascoInsurance().setEndDate(form.getCascoEndDate());
        car.getCascoInsurance().setAmount(form.getCascoAmount());
        car.getCascoInsurance().setPaymentPlan(form.getCascoPaymentPlan());

        car.getTechnicalInspection().setInspectionDate(form.getInspectionDate());
        car.getTechnicalInspection().setValidUntil(form.getInspectionValidUntil());

        car.getVignette().setStartDate(form.getVignetteStartDate());
        car.getVignette().setValidUntil(form.getVignetteValidUntil());
    }
}
