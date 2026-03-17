package com.mytransport.repository.carlog;

import com.mytransport.models.carlog.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findAllByOrderByCreatedAtDesc();

    List<Car> findAllByOwnerUsernameOrderByCreatedAtDesc(String username);

    Optional<Car> findByIdAndOwnerUsername(Long id, String username);
}
