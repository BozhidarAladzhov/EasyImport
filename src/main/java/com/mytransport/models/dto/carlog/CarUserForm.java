package com.mytransport.models.dto.carlog;

import com.mytransport.models.carlog.CarAccountRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CarUserForm {

    private Long id;

    @NotBlank
    @Size(max = 60)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String fullName;

    @Size(min = 6, max = 100)
    private String password;

    private CarAccountRole role = CarAccountRole.USER;
    private boolean enabled = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CarAccountRole getRole() {
        return role;
    }

    public void setRole(CarAccountRole role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
