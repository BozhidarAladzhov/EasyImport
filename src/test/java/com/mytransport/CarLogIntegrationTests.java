package com.mytransport;

import com.mytransport.models.carlog.CarAccountRole;
import com.mytransport.models.carlog.FuelType;
import com.mytransport.models.dto.carlog.CarForm;
import com.mytransport.models.dto.carlog.CarUserForm;
import com.mytransport.services.CarLogService;
import com.mytransport.services.CarUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CarLogIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarUserService carUserService;

    @Autowired
    private CarLogService carLogService;

    private Long carId;

    @BeforeEach
    void setUp() {
        if (carUserService.findByUsername("petar").isEmpty()) {
            CarUserForm userForm = new CarUserForm();
            userForm.setUsername("petar");
            userForm.setFullName("Petar Petrov");
            userForm.setPassword("secret123");
            userForm.setRole(CarAccountRole.USER);
            userForm.setEnabled(true);
            Long ownerId = carUserService.create(userForm).getId();

            CarForm carForm = new CarForm();
            carForm.setOwnerId(ownerId);
            carForm.setProductionYear(2018);
            carForm.setMake("BMW");
            carForm.setModel("320d");
            carForm.setFuelType(FuelType.DIESEL);
            carForm.setMileage(165000L);
            carForm.setRegistrationNumber("CB1234AB");
            carForm.setInspectionDate(LocalDate.now());
            carForm.setInspectionValidUntil(LocalDate.now().plusYears(1));
            carId = carLogService.createCar(carForm).getId();
        } else if (carId == null) {
            carId = carLogService.findVisibleCars("petar", false).stream().findFirst().orElseThrow().getId();
        }
    }

    @Test
    void dashboardRendersForAdmin() throws Exception {
        mockMvc.perform(get("/carlog")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("carlog/dashboard"));
    }

    @Test
    void detailRendersForOwner() throws Exception {
        mockMvc.perform(get("/carlog/cars/" + carId)
                        .with(SecurityMockMvcRequestPostProcessors.user("petar").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("carlog/car-detail"));
    }
}
