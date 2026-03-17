package com.mytransport.services;

import com.mytransport.models.carlog.CarUser;
import com.mytransport.models.dto.carlog.CarUserForm;
import com.mytransport.repository.carlog.CarUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarUserService {

    private final CarUserRepository carUserRepository;
    private final PasswordEncoder passwordEncoder;

    public CarUserService(CarUserRepository carUserRepository, PasswordEncoder passwordEncoder) {
        this.carUserRepository = carUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<CarUser> findByUsername(String username) {
        return carUserRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<UserDetails> loadDatabaseUser(String username) {
        return carUserRepository.findByUsername(username)
                .filter(CarUser::isEnabled)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPasswordHash())
                        .roles(user.getRole().name())
                        .build());
    }

    @Transactional(readOnly = true)
    public List<CarUser> findAll() {
        return carUserRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CarUser> findAssignableOwners() {
        return carUserRepository.findAll().stream()
                .filter(CarUser::isEnabled)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<CarUser> findById(Long id) {
        return carUserRepository.findById(id);
    }

    @Transactional
    public CarUser create(CarUserForm form) {
        if (carUserRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }
        CarUser user = new CarUser();
        apply(user, form, true);
        return carUserRepository.save(user);
    }

    @Transactional
    public CarUser update(Long id, CarUserForm form) {
        CarUser user = carUserRepository.findById(id).orElseThrow();
        if (!user.getUsername().equals(form.getUsername()) && carUserRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        apply(user, form, false);
        return carUserRepository.save(user);
    }

    private void apply(CarUser user, CarUserForm form, boolean requirePassword) {
        user.setUsername(form.getUsername().trim());
        user.setFullName(form.getFullName().trim());
        user.setRole(form.getRole());
        user.setEnabled(form.isEnabled());
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(form.getPassword().trim()));
        } else if (requirePassword) {
            throw new IllegalArgumentException("Password is required.");
        }
    }
}
