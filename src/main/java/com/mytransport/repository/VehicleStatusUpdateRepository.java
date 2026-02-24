package com.mytransport.repository;

import com.mytransport.models.VehicleStatusUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleStatusUpdateRepository extends JpaRepository<VehicleStatusUpdate, Long> {
    List<VehicleStatusUpdate> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);
}
