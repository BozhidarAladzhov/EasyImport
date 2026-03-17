package com.mytransport.repository.carlog;

import com.mytransport.models.carlog.CarUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarUserRepository extends JpaRepository<CarUser, Long> {
    Optional<CarUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
